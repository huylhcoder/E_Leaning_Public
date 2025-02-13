package com.fpoly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fpoly.entity.Cart;
import com.fpoly.entity.Course;
import com.fpoly.entity.User;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.service.CartService;
import com.fpoly.service.CourseService;
import com.fpoly.service.RegisteredCourseService;
import com.fpoly.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin("*") // Cho phép bên ngoài truy xuất vào thoải mái, không ngăn cản gì cả
@RestController
@RequestMapping("${api.prefix}/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	@Autowired
	private RegisteredCourseService registeredCourseService;
	@Autowired
	private JwtTokenUtils jwtTokenUtils;
	@Autowired
	private UserService usService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService UserService;

	// - Phần code của HBao ---
	// - Hiển thị giỏ hàng theo user id (user id lấy từ session đăng nhập)
	@GetMapping("/user/{token}")
	public ResponseEntity<?> fillListCourseCartByUserID(@PathVariable String token) {
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(token);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			// Log lỗi và trả về phản hồi HTTP 400
			System.err.println("Lỗi trích xuất email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body("Token không hợp lệ.");
		}

		User user;
		try {
			user = usService.getUserByEmailToan(email);
			System.out.println("User: " + user);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy User.");
			}
		} catch (Exception e) {
			// Log lỗi và trả về phản hồi HTTP 500
			System.err.println("Error fetching user: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi lấy User.");
		}

		try {
			List<Cart> listGioHang = cartService.fillListCourseCartByUserID(user.getUserId());
			return ResponseEntity.ok(listGioHang);
		} catch (RuntimeException e) {
			// Log lỗi và trả về phản hồi HTTP 400
			System.err.println("Runtime error fetching cart: " + e.getMessage());
			return ResponseEntity.badRequest().body("Lỗi lấy giỏ hàng: " + e.getMessage());
		} catch (Exception e) {
			// Log lỗi và trả về phản hồi HTTP 500
			System.err.println("General error fetching cart: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi lấy giỏ hàng.");
		}
	}

	// Thêm khóa học vào giỏ hàng
	// - Thêm khóa học vào giỏ hàng
	@PostMapping("/addCourse/{courseId}")
	public ResponseEntity<?> addCourseToCart(@PathVariable int courseId, @RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		System.out.println("CourseID: " + courseId);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body("Token không hợp lệ.");
		}

		User user = usService.getUserByEmailToan(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy User.");
		}
		try {
			// Kiểm tra nếu khóa học đã được đăng ký
	        List<Course> registeredCourses = courseService.findCoursesOnRegisteredCourse(courseId, user.getUserId());
	        if (!registeredCourses.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Khóa học đã được đăng ký trước đó!\"}");
	        }
			
			Cart createCart = cartService.addCourseToCart(courseId, user.getUserId());
			System.out.println("Thêm cart thành công: "+createCart);
			return ResponseEntity.ok(createCart);
		} catch (RuntimeException e) {
			// Trả về lỗi nếu khóa học đã tồn tại trong giỏ hàng
			System.err.println("Lỗi chung khi thêm khóa học vào giỏ hàng: " + e.getMessage());
			return ResponseEntity.status(400).body("{\"message\": \"Khóa học đã có trong giỏ hàng!\"}");
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Cart> removeCartBao(@PathVariable("id") int id) {
		Cart kiemTraTonTai = cartService.timKiemCartTheoId(id);
		if (kiemTraTonTai != null) {
			cartService.removeCart(id);
			return ResponseEntity.ok(kiemTraTonTai);
		}
		return ResponseEntity.ok(kiemTraTonTai);
	}

	@PostMapping("/addCourseInMyList/{courseId}")
	public ResponseEntity<Map<String, String>> addRegisteredCourse(@PathVariable int courseId,
	        @RequestHeader("Authorization") String token) { // Chỉnh sửa ở đây
	    String email;

	    email = jwtTokenUtils.extractEmail(token.replace("Bearer ", "")); // Loại bỏ "Bearer " trước khi lấy email

	    User user = UserService.getUserByEmailToan(email);

	    Map<String, String> response = new HashMap<>();
	    int userId = user.getUserId();
	    try {
	        registeredCourseService.addRegisteredCourse(courseId, userId);
	        response.put("message", "Course added to cart successfully.");
	        return ResponseEntity.ok(response);
	    } catch (RuntimeException e) {
	        response.put("error", e.getMessage());     
	        return ResponseEntity.status(400).body(response); // Trả về thông báo lỗi nếu khóa học đã tồn tại
	    } catch (Exception e) {
	        response.put("error", "Error registering course: " + e.getMessage());
	        return ResponseEntity.status(500).body(response);
	    }
	}
	// - Kết thúc phần code của HBao ---
}