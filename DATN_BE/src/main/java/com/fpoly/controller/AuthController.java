package com.fpoly.controller;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.controller.OtpController.DateTimeUtils;
import com.fpoly.dto.UserLoginDTO;
import com.fpoly.dto.UserRegisterDTO;
import com.fpoly.entity.MailInfo;
import com.fpoly.entity.User;
import com.fpoly.response.RegisterResponse;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.security.LocalizationUtils;
import com.fpoly.security.MessageKeys;
import com.fpoly.security.RefreshTokenService;
import com.fpoly.service.EmailService;
import com.fpoly.service.RoleService;
import com.fpoly.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("${api.prefix}/users")
public class AuthController {

	private static final String SUCCESS = "SUCCESS";
	private static final String USER_CREATED_SUCCESS = "User created successfully";

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	EmailService emailService;

	@Autowired
	LocalizationUtils localizationUtils;
	@Autowired
	JwtTokenUtils jwtTokenUtils;
	@Autowired
	RefreshTokenService refreshTokenService;

	@PostMapping("/check-user")
	public ResponseEntity<?> checkUser(@RequestBody UserRegisterDTO userRegisterDTO) {
		if (userService.existsByEmail(userRegisterDTO.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\": \"Email đã được sử dụng!\"}");
		}
		if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getRetypePassword())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\": \"Mật khẩu không khớp!\"}");
		}
		return ResponseEntity.status(HttpStatus.OK).body(userService.existsByEmail(userRegisterDTO.getEmail()));
	}
	
	// Của Huy
//	@PostMapping("/send-verification-code")
//	public ResponseEntity<String> sendVerificationCode(@RequestBody String email) {
//		String code = String.format("%06d", new Random().nextInt(999999));
//		emailService.sendEmail(email, code);
//		return ResponseEntity.ok(code);
//	}
	// Của Bảo
	
	@PostMapping("/send-verification-code")
	public ResponseEntity<String> sendVerificationCode(@RequestBody String email) {
		String code = String.format("%06d", new Random().nextInt(999999));
		String htmlContent = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\">"
    	        + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
    	        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
    	        + "<title>Mã xác minh tạo tài khoản</title>"
    	        + "<style>body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
    	        + ".container { max-width: 900px; margin: 20px auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
    	        + ".header { background-color: #66cc66; padding: 10px; text-align: left; color: #ffffff; font-size: 25px; font-weight: bold; }"
    	        + ".title { font-size: 24px; color: #4CAF50; margin: 20px 0; text-align: center; }"
    	        + ".content { font-size: 16px; color: #333; line-height: 1.6; }"
    	        + ".otp { font-size: 36px; color: #4CAF50; text-align: center; margin: 20px 0; font-weight: bold; }"
    	        + ".footer { font-size: 14px; color: #666; margin-top: 20px; }</style></head>"
    	        + "<body><div class=\"container\"><div class=\"header\">E - Learning</div>"
    	        + "<div class=\"title\">Mã xác minh tạo tài khoản mới</div>"
    	        + "<div class=\"content\"><p>Xin chào bạn </p>"
    	        + "<p>Chúng tôi nhận được yêu cầu tạo tài khoản mới của bạn vào lúc " + DateTimeUtils.getCurrentDateTime() + "</p>"
    	        + "<p>Dưới đây là mã xác nhận để tiếp tục yêu cầu của bạn:</p></div>"
    	        + "<div class=\"otp\">[ " + code + " ]</div>" // Replace '123456' with the actual OTP value
    	        + "<div class=\"footer\"><p>Vui lòng không chia mã xác nhận ra bên ngoài</p>"
    	        + "<p>Nếu bạn không yêu cầu tiếp tục tạo tài khoản, vui lòng bỏ qua email này.</p>"
    	        + "<p>Xin cảm ơn.</p></div></div></body></html>";
		emailService.sendOTP(new MailInfo(email, "Đặt lại mật khẩu", htmlContent));
		return ResponseEntity.ok(code);
	}
	
	@PostMapping("/register")
	// can we register an "admin" user ?
	public ResponseEntity<?> createUser(@RequestBody UserRegisterDTO userDTO) {
		System.out.println(userDTO.getEmail());
		System.out.println(userDTO.getPassword());
		System.out.println(userDTO.getRetypePassword());
		RegisterResponse registerResponse = new RegisterResponse(null, null);
		try {
			// Kiểm tra mật khẩu có giống mật khẩu xác nhận không
			if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
				registerResponse.setMessage(MessageKeys.PASSWORD_NOT_MATCH);
				return ResponseEntity.badRequest().body("Mật khẩu xác nhận không khớp");
			}
			System.out.println("Đăng ký - Đã kiểm tra xác nhận mật khẩu");

			// Tạo một người dùng mới
			User user = userService.createUser(userDTO);
			System.out.println("Đăng ký - set thông báo thành công");
			registerResponse.setMessage(MessageKeys.REGISTER_SUCCESSFULLY);
			System.out.println("Đăng ký thành công");
			return ResponseEntity.ok(registerResponse);
		} catch (Exception e) {
			System.out.println("Đăng ký thất bại");
			return ResponseEntity.badRequest().body(registerResponse);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
	    System.out.println("Input Đăng nhập Controller: "+userLoginDTO.getEmail() + userLoginDTO.getPassword());
	    try {
	        String accessToken = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
	        String email = jwtTokenUtils.extractEmail(accessToken);
	        User user = userService.getUserByEmailToan(email);
	        // Trả về một đối tượng JSON chứa token và roleName
	        Map<String, Object> response = new HashMap<>();
	        response.put("token", accessToken);
	        response.put("role", user.getRole().getName()); // Thêm role vào phản hồi
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}

}
