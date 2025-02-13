package com.fpoly.controller;

import java.awt.*;
import java.nio.file.Files;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.text.Normalizer;
import com.fpoly.entity.MailInfo;
import com.fpoly.entity.User;
import com.cloudinary.Cloudinary;
import com.fpoly.entity.Course;
import com.fpoly.entity.CourseProgress;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.service.CourseProgressService;
import com.fpoly.service.CourseService;
import com.fpoly.service.EmailService;
import com.fpoly.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RestController
@RequestMapping("${api.prefix}/user")
@SessionAttributes({ "email", "otp", "otpSentTime" })
public class OtpController {
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	EmailService emailService;
	@Autowired
	private JwtTokenUtils jwtTokenUtils;
	@Autowired
	HttpSession session;
	@Autowired
	private Cloudinary cloudinary;
	@Autowired
	private CourseProgressService courseProgressService;

	// Phương thức gửi OTP
	@PostMapping("/ForgotPass/send-otp")
	public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		if (email == null || email.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("message", "Cần cung cấp email"));
		}
		if (!userService.checkEmailExists(email)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("message", "Email không tồn tại"));
		}
		// Tạo mã OTP
		String otp = generateOtp();
		try {
			User user = userService.getUserByEmailToan(email);
			String userName = user.getName(); // Giả sử có phương thức getFullName()
			// Nội dung HTML cho email
			String htmlContent = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\">"
					+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
					+ "<title>Mã xác minh đổi mật khẩu</title>"
					+ "<style>body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
					+ ".container { max-width: 100%; margin: 20px auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
					+ ".header { background-color: #66cc66; padding: 10px; text-align: left; color: #ffffff; font-size: 25px; font-weight: bold; }"
					+ ".title { font-size: 24px; color: #4CAF50; margin: 20px 0; text-align: center; }"
					+ ".content { font-size: 16px; color: #333; line-height: 1.6; }"
					+ ".otp { font-size: 36px; color: #4CAF50; text-align: center; margin: 20px 0; font-weight: bold; }"
					+ ".footer { font-size: 14px; color: #666; margin-top: 20px; }</style></head>"
					+ "<body><div class=\"container\"><div class=\"header\">E - Learning</div>"
					+ "<div class=\"title\">Mã xác minh đổi mật khẩu</div>" + "<div class=\"content\"><p>Xin chào bạn "
					+ userName + "</p>" + "<p>Chúng tôi nhận được yêu cầu đổi mật khẩu của bạn vào lúc "
					+ DateTimeUtils.getCurrentDateTime() + "</p>"
					+ "<p>Dưới đây là mã xác nhận để tiếp tục yêu cầu của bạn:</p></div>" + "<div class=\"otp\">[ "
					+ otp + " ]</div>" // Replace '123456' with the actual OTP value
					+ "<div class=\"footer\"><p>Vui lòng không chia mã xác nhận ra bên ngoài</p>"
					+ "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>"
					+ "<p>Xin cảm ơn.</p></div></div></body></html>";

			// Gửi email OTP
			emailService.sendOTP(new MailInfo(email, "Đặt lại mật khẩu", htmlContent));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("message", "Lỗi gửi email"));
		}

		// Trả về OTP trong phản hồi
		Map<String, String> response = new HashMap<>();
		response.put("message", "OTP đã được gửi");
		response.put("otp", otp);
		return ResponseEntity.ok(response);
	}

	// Phương thức xác thực OTP
	@PostMapping("/ForgotPass/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
		String inputOtp = request.get("otp");
		String sessionOtp = request.get("serverOtp");
		String sessionEmail = request.get("email");
		System.out.println("email trong verifyOTP là: " + sessionEmail);
		System.out.println("mã otp trong verifyOTP là: " + sessionOtp);
		LocalDateTime otpSentTime = (LocalDateTime) session.getAttribute("otpSentTime");
		if (sessionOtp == null || otpSentTime == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("message", "Mã OTP không tồn tại hoặc đã hết hạn"));
		}
		if (ChronoUnit.MINUTES.between(otpSentTime, LocalDateTime.now()) > 5) {
			session.removeAttribute("otp");
			session.removeAttribute("otpSentTime");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("message", "Mã OTP đã hết hạn"));
		}
		if (sessionOtp.equals(inputOtp)) {
			return ResponseEntity.ok(Collections.singletonMap("message", "Mã OTP xác thực thành công!"));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("message", "Mã OTP không chính xác"));
		}
	}

	// Phương thức cập nhật mật khẩu
	@PostMapping("/ForgotPass/update-password")
	public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
		String newPassword = request.get("newPassword");
		String sessionEmail = request.get("email");
		// String sessionEmail = (String) session.getAttribute("email");
		System.out.println("email trong updatePass là: " + sessionEmail);
		System.out.println("Mật khẩu mới trong updatePass là: " + newPassword);
		if (sessionEmail == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("message", "Email không tồn tại trong phiên làm việc"));
		}
		// Kiểm tra email có tồn tại
		if (!userService.checkEmailExists(sessionEmail)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("message", "Email không tồn tại"));
		}
		// Cập nhật mật khẩu mới
		try {
			userService.updatePassword(sessionEmail, newPassword);
			// Xóa thông tin email và OTP trong session sau khi đổi mật khẩu thành công
			return ResponseEntity.ok(Collections.singletonMap("message", "Mật khẩu đã được cập nhật thành công!"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("message", "Có lỗi xảy ra khi cập nhật mật khẩu"));
		}
	}

	// Phương thức sinh mã OTP
	private String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000); // Đảm bảo OTP có 6 chữ số
		return String.valueOf(otp);
	}

	// Phương thức lấy ngày giờ hiện tại
	public class DateTimeUtils {
		// Định dạng ngày giờ theo yêu cầu
		private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		// Phương thức lấy ngày giờ hiện tại dưới dạng chuỗi
		public static String getCurrentDateTime() {
			LocalDateTime now = LocalDateTime.now();
			return now.format(DATE_TIME_FORMATTER);
		}
	}

	// Đóng góp ý kiến
	@PostMapping(value = "/sendComment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> sendComment(@RequestHeader("Authorization") String token,
	                                      @RequestParam("phanhoi") String loaiPhanHoi,
	                                      @RequestParam("noidung") String noiDung,
	                                      @RequestParam(value = "file", required = false) MultipartFile file) {

	    String FToken = token.replace("Bearer ", "").trim();
	    System.out.println("Token: " + token);
	    String email = "";
	    //String emailSend = "baochpc07738@fpt.edu.vn";
	    String emailSend = "khoatdpc07807@fpt.edu.vn"; 
	    try {
	        email = jwtTokenUtils.extractEmail(FToken);
	        System.out.println("Email: " + email);
	    } catch (Exception e) {
	        System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
	        return ResponseEntity.badRequest().body(null);
	    }

	    User user = userService.getUserByEmailToan(email);
	    try {
	        String userName = user.getName(); // Lấy tên người dùng

	        // Chuẩn bị nội dung HTML cho email
	        String htmlContent = "<!DOCTYPE html><html lang=\"vi\"><head><meta charset=\"UTF-8\">"
	                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
	                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
	                + "<title>Phản hồi từ người dùng</title>"
	                + "<style>"
	                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
	                + ".container { max-width: 100%; margin: 20px auto; padding: 20px; background-color: #ffffff; "
	                + "border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
	                + ".header { background-color: #66cc66; padding: 15px; text-align: left; color: #ffffff; font-size: 25px; font-weight: bold; }"
	                + ".title { font-size: 18px; color: #333333; margin: 20px 0; font-weight: bold; }"
	                + ".content { font-size: 16px; color: #333; line-height: 1.6; }"
	                + ".footer { font-size: 14px; color: #666; margin-top: 20px; }"
	                + ".content p { margin: 10px 0; }"
	                + "</style></head>"
	                + "<body>"
	                + "<div class=\"container\">"
	                + "<div class=\"header\">E - Learning</div>"
	                + "<div class=\"content\">"
	                + "<p>Gửi Người quản lý,</p>"
	                + "<p>Bạn đã nhận được phản hồi của người dùng " + userName + " với loại phản hồi <strong>" + loaiPhanHoi + "</strong>.</p>"
	                + "<p><strong>Nội dung phản hồi từ người dùng:</strong></p>"
	                + "<p>" + noiDung + "</p>"
	                + "<p>Trân trọng,</p>"
	                + "</div>"
	                + "</div>"
	                + "</body></html>";

	        // Gửi email với file đính kèm nếu có
	        String commentPath = null;
	        if (file != null && !file.isEmpty()) {
	            // Lưu tệp vào thư mục mong muốn
	            String fileName = file.getOriginalFilename();
	            commentPath = "C:\\Users\\Admin\\Downloads\\commentFile\\" + fileName; // Đường dẫn lưu tệp
	            Files.copy(file.getInputStream(), Paths.get(commentPath), StandardCopyOption.REPLACE_EXISTING);
	            System.out.println("Tệp đã được lưu: " + fileName);
	        }

	        // Gửi email
	        emailService.sendCommentFile(new MailInfo(emailSend, "Phản hồi từ người dùng", htmlContent), commentPath);
	        
	        return ResponseEntity.ok(Collections.singletonMap("message", "Gửi phản hồi thành công"));

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("message", "Lỗi gửi phản hồi"));
	    }
	}

	// Xóa dấu trong chuỗi 
	public static String removeDiacritics(String input) {
	    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
	    return normalized.replaceAll("\\p{M}", ""); // Loại bỏ các ký tự dấu
	}
	
	// Gửi chứng chỉ
	@PostMapping("/sendCertificate/{courseId}")
	public ResponseEntity<?> sendCertificate(@RequestHeader("Authorization") String token, @PathVariable int courseId) {
	    String FToken = token.replace("Bearer ", "").trim();
	    System.out.println("Token: " + token);
	    String email = "";
	    try {
	        email = jwtTokenUtils.extractEmail(FToken);
	        System.out.println("Email: " + email);
	    } catch (Exception e) {
	        System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
	        return ResponseEntity.badRequest().body(null);
	    }

	    User user = userService.getUserByEmailToan(email);
	    Course course = courseService.timKhoaHocTheoMaKhoaHocToan(courseId);
	    Integer UserId = user.getUserId();
	    try {
	        String userName = user.getName(); // Lấy tên người dùng
	        String courseName = course.getName(); // Lấy tên khóa học

	        // Tạo fileName
	        String formattedUserName = removeDiacritics(userName).replaceAll("\\s+", ""); // Loại bỏ khoảng trắng và dấu
	        String formattedCourseName = removeDiacritics(courseName).replaceAll("\\s+", ""); // Loại bỏ khoảng trắng và dấu
	        String fileName = "ChungChi_" + formattedCourseName + "_" + formattedUserName;
	        
	        // Tạo ảnh chứng chỉ
	        //String templatePath = "C:\\Users\\Laptop\\Pictures\\Saved Pictures\\NenChungChi.png"; // Đường dẫn file mẫu
	        //String outputPath = "C:\\Users\\Laptop\\Pictures\\Saved Pictures\\" + fileName + ".png"; // Đường dẫn lưu file kết quả
	        
	     // Tạo ảnh chứng chỉ
	        String templatePath = "C:\\Users\\Admin\\Downloads\\certificate\\NenChungChi.png"; // Đường dẫn file mẫu
	        String outputPath = "C:\\Users\\Admin\\Downloads\\certificate\\" + fileName + ".png"; // Đường dẫn lưu file kết quả

	        BufferedImage certificateTemplate = ImageIO.read(new File(templatePath));
	        Graphics2D graphics = certificateTemplate.createGraphics();	        
	        
	        // Tải ảnh lên Cloudinary
	        //Map uploadResult = cloudinary.uploader().upload(outputPath, ObjectUtils.emptyMap());
	        //String imageUrl = (String) uploadResult.get("secure_url"); // Lấy URL của ảnh đã tải lên
	        
	        // Thiết lập font và màu chữ
	        graphics.setFont(new Font("Arial", Font.BOLD, 150));
	        graphics.setColor(Color.decode("#dcb458"));
	        
	        // Đo kích thước chuỗi văn bản
	        FontMetrics fontMetricsN = graphics.getFontMetrics();
	        int textWidthN = fontMetricsN.stringWidth(userName);
	        
	        // Tính toán toạ độ x để căn giữa
	        int xName = (2000 - textWidthN) / 2; // 2000 là chiều rộng ảnh
	        int yName = 650; // Vị trí y cố định theo yêu cầu
	        
	        // Thêm tên người dùng
	        graphics.drawString(userName, xName, yName); // Toạ độ cần chỉnh cho đúng với mẫu

	        // Thiết lập font và màu chữ
	        graphics.setFont(new Font("Arial", Font.BOLD, 60));
	        graphics.setColor(Color.decode("#0d3b66"));
	        
	        // Đo kích thước chuỗi văn bản
	        FontMetrics fontMetrics = graphics.getFontMetrics();
	        int textWidth = fontMetrics.stringWidth(courseName);
	        
	        // Tính toán toạ độ x để căn giữa
	        int x = (2000 - textWidth) / 2; // 2000 là chiều rộng ảnh
	        int y = 930; // Vị trí y cố định theo yêu cầu
	        
	        // Thêm tên khóa học
	        graphics.drawString(courseName, x, y);
	        
	        // Tạo QR code
	        /*
	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        String qrCodeData = outputPath; // Đường dẫn của file chứng chỉ
	        //String qrCodeData = imageUrl; // Sử dụng URL của ảnh
	        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 300, 300); // Kích thước QR code
	        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
	         */
	        // Vẽ QR code lên ảnh chứng chỉ
	        /*
	        int qrCodeX = 1600; // Tọa độ x để vẽ QR code
	        int qrCodeY = 1030; // Tọa độ y để vẽ QR code
	        graphics.drawImage(qrCodeImage, qrCodeX, qrCodeY, null); // Vẽ QR code lên ảnh chứng chỉ
	     	*/        
	        graphics.dispose();

	        // Lưu ảnh chứng chỉ
	        ImageIO.write(certificateTemplate, "png", new File(outputPath));

	        // Chuẩn bị nội dung HTML cho email
	        String htmlContent = "<!DOCTYPE html><html lang=\"vi\"><head><meta charset=\"UTF-8\">"
	                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
	                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
	                + "<title>E - Learning</title>"
	                + "<style>"
	                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
	                + ".container { max-width: 100%; margin: 20px auto; padding: 20px; background-color: #ffffff; "
	                + "border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
	                + ".header { background-color: #66cc66; padding: 15px; text-align: left; color: #ffffff; font-size: 25px; font-weight: bold; }"
	                + ".title { font-size: 18px; color: #333333; margin: 20px 0; font-weight: bold; }"
	                + ".content { font-size: 16px; color: #333; line-height: 1.6; }"
	                + ".footer { font-size: 14px; color: #666; margin-top: 20px; }"
	                + ".content p { margin: 10px 0; }"
	                + "</style></head>"
	                + "<body>"
	                + "<div class=\"container\">"
	                + "<div class=\"header\">E - Learning</div>"
	                + "<div class=\"content\">"
	                + "<p>Kính gửi <strong>" + userName + "</strong>,</p>"
	                + "<p>Tôi rất vui được thông báo rằng bạn đã hoàn thành khóa học <strong>" + courseName + "</strong>. Xin gửi kèm theo chứng chỉ hoàn thành của bạn.</p>"
	                + "<p>Thông tin chi tiết về chứng chỉ:</p>"
	                + "<ul>"
	                + "<li><strong>Tên khóa học: </strong> " + courseName + "</li>"
	                + "<li><strong>Ngày hoàn thành: </strong>" + LocalDateTime.now() + "</li>"
	                + "<li><strong>Họ và tên: </strong> " + userName + "</li>"
	                + "</ul>"
	                + "<p>Chúc mừng bạn đã hoàn thành khóa học này! Nếu bạn có bất kỳ câu hỏi nào, xin vui lòng liên hệ với tôi.</p>"
	                + "<p>Trân trọng,</p>"
	                + "</div>"
	                + "</div>"
	                + "</body></html>";

	        // Gửi email với file đính kèm
	        emailService.sendCertificateWithAttachment(new MailInfo(email, "Chứng chỉ hoàn thành khóa học", htmlContent), outputPath);
	        userService.updateStatusProgress(UserId, courseId);
	        return ResponseEntity.ok(Collections.singletonMap("message", "Gửi chứng chỉ thành công"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("message", "Lỗi gửi chứng chỉ"));
	    }
	}
	
	@GetMapping("/GetCourseProgress/{courseId}")
	public ResponseEntity<?> GetCourseProgress(@RequestHeader("Authorization") String token, @PathVariable int courseId) {
	    String FToken = token.replace("Bearer ", "").trim();
	    System.out.println("Token: " + token);
	    String email;

	    try {
	        email = jwtTokenUtils.extractEmail(FToken);
	        System.out.println("Email: " + email);
	    } catch (Exception e) {
	        System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
	        return ResponseEntity.badRequest().body("Lỗi trích xuất email từ token.");
	    }

	    User user = userService.getUserByEmailToan(email);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
	    }

	    Course course = courseService.timKhoaHocTheoMaKhoaHocToan(courseId);
	    if (course == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khóa học không tồn tại.");
	    }

	    Integer userId = user.getUserId();
	    Optional<CourseProgress> courseProgress = userService.findByCourseId(userId, courseId);

	    if (courseProgress.isPresent()) {
	        return ResponseEntity.ok(courseProgress.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tiến trình khóa học không tồn tại.");
	    }
	}

}