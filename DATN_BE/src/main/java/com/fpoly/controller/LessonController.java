package com.fpoly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fpoly.entity.Course;
import com.fpoly.entity.Lesson;
import com.fpoly.service.CourseService;
import com.fpoly.service.LessonService;

@CrossOrigin("*") // cho phép bên ngoài truy xuất vào thoải mái k ngăn cản gì cả
@RestController
@RequestMapping("/api/lession")
public class LessonController {
	@Autowired
	private LessonService LessonService;
	
	@Autowired CourseService CourseService;
	
//	@GetMapping("/course/{id}")
//    public ResponseEntity<List<Lesson>> getLessionByCourse(@PathVariable("id") int id) {
//        Course course = CourseService.getCourseID(id).orElse(null);
//        if (course == null) {
//            return ResponseEntity.notFound().build();
//        }
//        List<Lesson> listLession = LessonService.getLessonByCourse(course);
//        return ResponseEntity.ok(listLession);
//    }
}
