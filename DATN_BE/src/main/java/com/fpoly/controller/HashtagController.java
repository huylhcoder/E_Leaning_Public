package com.fpoly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.entity.Course;
import com.fpoly.entity.HashTag;
import com.fpoly.entity.HashTagOfCourse;
import com.fpoly.repository.HashtagRepository;
import com.fpoly.service.CourseService;
import com.fpoly.service.HashtagService;

import jakarta.persistence.Tuple;

@CrossOrigin("*") // cho phép bên ngoài truy xuất vào thoải mái k ngăn cản gì cả
@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {

	@Autowired
	HashtagRepository hashtagRepository;
	
	@Autowired
	HashtagService hashtagService;
	
	
	
	@GetMapping("/hashtags")
	public List<HashTagOfCourse> getAllCourse() {
		return hashtagService.getAllHashtag();
	}
	
//	@GetMapping("/{courseId}")
//	public List<String> getHashTagsByCourseId(@PathVariable int courseId) {
//	    return hashtagService.getHashTagsByCourseId(courseId);
//	}
	@GetMapping("/{courseId}")
	public List<HashTagOfCourse> getHashTagsByCourseId(@PathVariable int courseId) {
	    return hashtagService.getHashTagsByCourseId(courseId);
	}
	
	@GetMapping("hashtagKhoa/{hashTagId}")
	public List<Course> getCoursesByHashTagId(@PathVariable int hashTagId) {
        return hashtagService.getCoursesByHashTagId(hashTagId);
    }

}
