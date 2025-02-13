package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fpoly.entity.Cart;
import com.fpoly.entity.Course;
import com.fpoly.entity.FavoriteCourse;
import com.fpoly.entity.User;
import com.fpoly.repository.AnswerRepository;
import com.fpoly.repository.CartRepository;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.FavoriteCourseRepository;
import com.fpoly.repository.UserRepository;

@Service
public class FavoriteCourseService {
	@Autowired
	FavoriteCourseRepository favoriteCourseRepository;
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	UserRepository userRepository;
	

	public List<FavoriteCourse> fillListCourseFcByUserID(int user) {
		return favoriteCourseRepository.fillListCourseFcByUserID(user);
	}

	// - Thêm Khóa học vào yêu thích hào
	public void addCourseToFavoriteCourse(int courseId, int userId) {
	    Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
	    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

	    // Kiểm tra xem khóa học đã có trong yêu thích hay chưa
	    List<FavoriteCourse> existingFavoriteCourse = favoriteCourseRepository.fillListCourseFcByUserID(userId);
	    for (FavoriteCourse favoriteCourse : existingFavoriteCourse) {
	        if (favoriteCourse.getCourse().getCourseId() == courseId) {
	            throw new RuntimeException("Course already exists in the favoriteCourse");
	        }
	    }

	    // Nếu không có, thêm khóa học vào yêu thích
	    FavoriteCourse favoriteCourse = new FavoriteCourse();
	    favoriteCourse.setCourse(course);
	    favoriteCourse.setUser(user);

	    favoriteCourseRepository.save(favoriteCourse);
	}
	public void removeFavoriteCourse(int favoriteCourseId) {
		favoriteCourseRepository.deleteById(favoriteCourseId);
	}
	public FavoriteCourse timKiemFavoriteCourseTheoId(int id) {
		return favoriteCourseRepository.findByFavoriteCourseId(id);
	}
}
