package com.fpoly.cloudinary;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.EagerTransformation;
import com.cloudinary.utils.ObjectUtils;

import io.jsonwebtoken.io.IOException;

@Service
public class CloudinaryService {
	@Autowired
	private Cloudinary cloudinary;

	public Map<?, ?> upload(MultipartFile file) throws IOException, java.io.IOException {
		try {
			Map<String, Object> options = ObjectUtils.asMap(
			// Bạn có thể thêm tùy chọn ở đây nếu cần
			);
			Map<?, ?> data = this.cloudinary.uploader().upload(file.getBytes(), options);
			return data;
		} catch (IOException io) {
			throw new RuntimeException("File upload failed");
		}
	}

	public Map<?, ?> uploadVideo(MultipartFile file) throws IOException {
	    try {
	        // Kiểm tra định dạng file
	        String contentType = file.getContentType();
	        if (contentType == null || !contentType.startsWith("video/")) {
	            throw new IllegalArgumentException("File không phải là video hợp lệ.");
	        }

	        // Đọc nội dung file vào byte array
	        byte[] bytes = file.getBytes();

	        Map<String, Object> options = ObjectUtils.asMap(
	            "resource_type", "video",
	            "public_id", "dog_closeup", // Bạn có thể thay đổi public_id theo ý muốn
	            "eager", Arrays.asList(
	                new EagerTransformation().width(300).height(300).crop("pad").audioCodec("none"),
	                new EagerTransformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")
	            ),
	            "eager_async", true,
	            "eager_notification_url", "https://mysite.example.com/notify_endpoint" // Thay đổi URL này theo ý muốn
	        );

	        // Upload video sử dụng byte array
	        Map<?, ?> data = cloudinary.uploader().upload(bytes, options);
	        return data;

	    } catch (IOException io) {
	        throw new RuntimeException("Up video thất bại: " + io.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace(); // In ra stack trace để dễ dàng gỡ lỗi
	        throw new RuntimeException("Up video thất bại: " + e.getMessage());
	    }
	}
}
