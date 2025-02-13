package com.fpoly.cloudinary;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fpoly.security.JwtTokenFilter;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.service.RegisteredCourseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

@RestController
@RequestMapping("${api.prefix}/upload-file")
public class FileController {
	@Autowired
	private CloudinaryService cloudinaryService;

//	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<?> createProduct(@Valid @RequestBody MultipartFile file) throws IOException {
//		System.out.println(file);
//		String imageUrl = "";
//		// Nếu tồn tại file thì upload lên Cloudinary
//		if (file != null && !file.isEmpty()) {
//			Map data = this.cloudinaryService.upload(file);
//			System.out.println("Controller: " + data.toString());
//			imageUrl = (String) data.get("url").toString();
//		}
//		return ResponseEntity.ok("Link ảnh: " + imageUrl);
//	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createProduct(@RequestParam("file") MultipartFile file) throws IOException {
		System.out.println(file);
		String fileUrl = "";
		// Nếu tồn tại file thì upload lên Cloudinary
		if (file != null && !file.isEmpty()) {
			Map<?, ?> data = this.cloudinaryService.upload(file);
			System.out.println("Controller: " + data.toString());
			fileUrl = (String) data.get("url").toString();
		}
		return ResponseEntity.ok("Link tệp: " + fileUrl);
	}

	@PostMapping(value = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createVideo(@RequestParam("video") MultipartFile video) throws IOException {
		System.out.println(video);
		System.out.println(video.getSize() / 1024 / 1024);
		String fileUrl = "";

		// Giới hạn dung lượng (10MB)
		long maxSize = 10 * 1024 * 1024; // 10MB

		// Kiểm tra kích thước file
		if (video.getSize() > maxSize) {
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File quá lớn. Giới hạn là 10MB.");
		}

		// Nếu tồn tại file thì upload lên Cloudinary
		if (video != null && !video.isEmpty()) {
			try {
				Map<?, ?> data = this.cloudinaryService.uploadVideo(video); // Gọi phương thức uploadVideo
				System.out.println("Controller: " + data.toString());
				fileUrl = (String) data.get("url").toString();
				return ResponseEntity.ok("Link tệp: " + fileUrl);
			} catch (RuntimeException e) {
				System.out.println(e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Upload video không thành công: " + e.getMessage());
			}
		}

		return ResponseEntity.badRequest().body("Không có file nào để upload.");
	}

	@Autowired
	HttpServletRequest req;

	@Autowired
	private VideoService videoService;

	@PostMapping(value = "/upload-video-on-server", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> UploadVideoOnServer(@RequestParam("video") MultipartFile video) throws IOException {
	    System.out.println(video);
	    System.out.println(video.getSize() / 1024 / 1024);

	    // Giới hạn dung lượng (200MB)
	    long maxSize = 200 * 1024 * 1024; // 200MB

	    // Kiểm tra kích thước file
	    if (video.getSize() > maxSize) {
	        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File quá lớn. Giới hạn là 200MB.");
	    }

	    // Nếu tồn tại file thì upload lên server
	    if (video != null && !video.isEmpty()) {
	        try {
	            String videoPath = videoService.uploadVideo(video);
	            // Tạo đường dẫn trả về
	            String fileName = Paths.get(videoPath).getFileName().toString();
	            String downloadLink = "http://localhost:8080/api/v1/upload-file/download-video-on-server?path=" + URLEncoder.encode(videoPath, "UTF-8");
	            return ResponseEntity.ok(downloadLink);
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading video");
	        }
	    }

	    return ResponseEntity.badRequest().body("Không có file nào để upload.");
	}

	private final String UPLOAD_DIR = "src/main/webapp/uploads/";

	@GetMapping("/download-video-on-server")
	public ResponseEntity<byte[]> downloadFileOnServer(@RequestParam("path") Optional<String> path) {
	    System.out.println("Downloading video controller - String path: " + path.orElse("Không có path"));

	    // Kiểm tra xem tham số path có tồn tại không
	    if (!path.isPresent()) {
	        System.out.println("Downloading video controller - Không có path");
	        return ResponseEntity.badRequest().body(null);
	    }

	    String videoPath = path.get();
	    byte[] videoBytes;

	    try {
	        // Tạo đối tượng File từ đường dẫn
	        File videoFile = new File(videoPath);
	        System.out.println(videoFile);
	        
	        // Kiểm tra xem tệp có tồn tại không
	        if (!videoFile.exists()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        // Đọc nội dung tệp video
	        videoBytes = Files.readAllBytes(videoFile.toPath());
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }

	    // Trả về video với header Content-Type
	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_OCTET_STREAM) // Sử dụng kiểu này
	            .body(videoBytes);
	}

	// Test hiển thị video
	// http://res.cloudinary.com/dxj6jmdm8/video/upload/v1732716762/Wed%20Nov%2027%2021:12:24%20ICT%202024.mp4
	// Làm sao để cho nó trả về một file video
	// Làm sao để cho nó hiển thị lên được FE

	// url này nó nhận vào một cái tên file
	// Kết hợp với url
	// files/{folder}/{file}
	// Tương đương hàm download trong Controller nó trả về mảng Byte[]
	// ng-src nó tự download cái nội dung files về và hiển thị lên
//    let url_anh_khoa_hoc = `http://localhost:8080/api/khoahoc/anhkhoahoc/anh_khoa_hoc`;
//    $scope.url = function (anh_dai_dien) {
//        return `${url_anh_khoa_hoc}/${anh_dai_dien}`;
//    }

//	<img ng-src="{{url(item.anh_dai_dien)}}" class="me-2"
//            style="width: 100px; height: 100px;">

	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	@Autowired
	private RegisteredCourseService registeredCourseService; // Dịch vụ để kiểm tra đăng ký khóa học

//    @GetMapping("/download-video")
//    public ResponseEntity<byte[]> loafVideoForLesson(
//            @RequestParam("link") Optional<String> link,
//            @RequestParam("courseId") Optional<Integer> courseId,
//            @RequestParam("tokenString") Optional<String> tokenString) {
//
//        // Kiểm tra xem link, courseId và tokenString có được cung cấp không
//        if (!link.isPresent() || !courseId.isPresent() || !tokenString.isPresent()) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        String videoUrl = link.get();
//        Integer courseIdValue = courseId.get();
//        String token = tokenString.get();
//
//        // Giải mã token để lấy email
////        String email;
////        try {
////            email = jwtTokenUtils.extractEmail(token);
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
////        }
////
////        // Kiểm tra người dùng có đăng ký khóa học chưa
////        boolean isEnrolled = checkUserEnrollment(email, courseIdValue);
////        if (!isEnrolled) {
////            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
////        }
//
//        // Tải video từ link và chuyển đổi thành byte[]
//        byte[] videoBytes;
//        try {
//            videoBytes = downloadVideo(videoUrl);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//
//        // Trả về video dưới dạng byte[]
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Đặt kiểu nội dung là video
//                .body(videoBytes);
//    }
//
//    // Phương thức kiểm tra người dùng đã đăng ký khóa học
//    private boolean checkUserEnrollment(String email, Integer courseId) {
//        // Gọi dịch vụ để kiểm tra xem người dùng đã đăng ký khóa học chưa
//        return registeredCourseService.isUserEnrolled(email, courseId);
//    }
//
//    // Phương thức tải video từ URL
//    private byte[] downloadVideo(String videoUrl) throws IOException {
//        URL url = new URL(videoUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        try (InputStream inputStream = connection.getInputStream()) {
//            return inputStream.readAllBytes();
//        }
//    }

	@GetMapping("/download-video")
	public ResponseEntity<?> loafVideoForLesson(@RequestParam("link") Optional<String> link,
			@RequestParam("courseId") Optional<Integer> courseId,
			@RequestParam("tokenString") Optional<String> tokenString) {
		if (!link.isPresent() || !courseId.isPresent() || !tokenString.isPresent()) {
			return ResponseEntity.badRequest().body(null);
		}

		String videoUrl = link.get();
		Integer courseIdValue = courseId.get();
		String token = tokenString.get();

		byte[] videoBytes;
		try {
			videoBytes = downloadVideo(videoUrl);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Server không thể tải về video!\"}");
		}

		System.out.println("Show video");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM) // Vẫn sử dụng kiểu này
				.body(videoBytes);
	}

	// Phương thức tải video từ URL
	private byte[] downloadVideo(String videoUrl) throws IOException {
		
		URL url = new URL(videoUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect(); // Kết nối đến URL

		System.out.println("Show video");
		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("HTTP error code: " + connection.getResponseCode());
		}

		try (InputStream inputStream = connection.getInputStream()) {
			return inputStream.readAllBytes();
		}
		
	}

}
