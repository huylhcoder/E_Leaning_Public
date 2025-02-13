package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Cart;
import com.fpoly.entity.Category;
import com.fpoly.entity.Course;
import com.fpoly.entity.User;
import com.fpoly.repository.CartRepository;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.UserRepository;

@Service
public class CartService {
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	UserRepository userRepository;

	// HBao Code

	// - Hiển thị danh sách khóa học trong giỏ hàng theo userid
	public List<Cart> fillListCourseCartByUserID(int user) {
		return cartRepository.fillListCourseCartByUserID(user);
	}

	// - Thêm Khóa học vào giỏ hàng
	public Cart addCourseToCart(int courseId, int userId) {
		Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User  not found"));
		// Kiểm tra xem khóa học đã có trong giỏ hàng hay chưa
		List<Cart> existingCarts = cartRepository.fillListCourseCartByUserID(userId);
		for (Cart cart : existingCarts) {
			if (cart.getCourse().getCourseId() == courseId) {
				throw new RuntimeException("Course already exists in the cart");
			}
		}
		// Kiểm tra xem khóa học đã được đăng ký hay chưa
		List<Course> registeredCourses = courseRepository.findCoursesOnRegisteredCourse(courseId, userId);
		if (!registeredCourses.isEmpty()) {
			throw new RuntimeException("Course is already registered");
		}
		// Nếu không có, thêm khóa học vào giỏ hàng
		Cart cart = new Cart();
		cart.setCourse(course);
		cart.setUser(user);

		return cartRepository.save(cart);
	}

	// - Xóa khóa học khỏi giỏ hàng
	public void removeCart(int cartId) {
		cartRepository.deleteById(cartId);
	}

	public Cart timKiemCartTheoId(int id) {
		return cartRepository.findByCartId(id);
	}

	public void deleteCartsByUserIdAndCourseId(int userId, int courseId) {
        cartRepository.deleteByUserIdAndCourseId(userId, courseId);
    }

	// Hết phần code của HBao
}
