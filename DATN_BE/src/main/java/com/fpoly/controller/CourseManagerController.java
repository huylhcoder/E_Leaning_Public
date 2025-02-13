package com.fpoly.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.dto.CourseManagerDTO;
import com.fpoly.entity.Course;
import com.fpoly.entity.User;
import com.fpoly.service.CourseService;
import com.fpoly.service.LessonService;
import com.fpoly.service.SectionService;
import com.fpoly.service.UserService;

@CrossOrigin("*") // cho phép bên ngoài truy xuất vào thoải mái k ngăn cản gì cả
@RestController
@RequestMapping("${api.prefix}/course-manager")
public class CourseManagerController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private UserService userService;

	// Hiển thị danh sách khóa học đã đăng
	// Hiển thị danh sách khóa học nháp

	@GetMapping("/posted-course")
	public ResponseEntity<?> postedCourses() {
		// Tìm kiểm khóa học đã đăng công khai(1) và không công khai (2)
		List<Course> listCourse = courseService.hienThiKhoaHocTheoTrangThaiKhoaHoc_Huy(Arrays.asList(1, 2));
		List<CourseManagerDTO> listCourseDTO = new ArrayList<CourseManagerDTO>();
		for (Course item : listCourse) {
			CourseManagerDTO courseDTO = new CourseManagerDTO();
			courseDTO.setCourseId(item.getCourseId());
			courseDTO.setAvatar(item.getAvatar());
			courseDTO.setName(item.getName());
			courseDTO.setCourseDuration(item.getCourseDuration());
			courseDTO.setStatus(item.getStatus());
			courseDTO.setCreateAt(item.getCreateAt());
			courseDTO.setNumberOfComment(0);
			courseDTO.setRevenue(0);
			//
			listCourseDTO.add(courseDTO);
		}
		return ResponseEntity.ok(listCourseDTO);
	}

	@GetMapping("/draft-course")
	public ResponseEntity<?> draftCourses() {
		// Tìm kiểm khóa học đã đăng công khai(1) và không công khai (2)
		List<Course> listCourse = courseService.hienThiKhoaHocTheoTrangThaiKhoaHoc_Huy(Arrays.asList(0));
		System.out.println(listCourse);
		List<CourseManagerDTO> listCourseDTO = new ArrayList<CourseManagerDTO>();
		for (Course item : listCourse) {
			CourseManagerDTO courseDTO = new CourseManagerDTO();
			courseDTO.setCourseId(item.getCourseId());
			courseDTO.setAvatar(item.getAvatar());
			courseDTO.setName(item.getName());
			courseDTO.setCourseDuration(item.getCourseDuration());
			courseDTO.setStatus(item.getStatus());
			courseDTO.setCreateAt(item.getCreateAt());
			courseDTO.setNumberOfComment(0);
			courseDTO.setRevenue(0);
			//
			listCourseDTO.add(courseDTO);
		}
		return ResponseEntity.ok(listCourseDTO);
	}

	@DeleteMapping("/draft-course/remove-course/{courseId}")
	public ResponseEntity<?> removeDraftCourses(@PathVariable int courseId) {
		try {
			courseService.removeDraftCourse(courseId);
			return ResponseEntity.ok("{\"message\": \"Xóa khóa học thành công!\"}");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Có lỗi xảy ra: " + e.getMessage());
		}
	}

	@PostMapping("")
	public ResponseEntity<?> addCourse() {
		// Tạo khóa học mới
		Course cousre = new Course();
		User user = userService.getUserByEmailToan("lehoanghuycoder@gmail.com");
		cousre.setUser(user);
		cousre.setStatus(0);
		cousre.setCreateAt(new Date());
		cousre.setUpdateAt(new Date());
		courseService.themKhoaHocMoi_Huy(cousre);
		// Khóa học mới tạo
		Course latestCourse = courseService.getLatestCourse();
		return ResponseEntity.ok(latestCourse.getCourseId());
	}
}
