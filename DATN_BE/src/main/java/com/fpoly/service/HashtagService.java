package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Course;
import com.fpoly.entity.HashTag;
import com.fpoly.entity.HashTagOfCourse;
import com.fpoly.repository.HashtagOfCourseRepository;
import com.fpoly.repository.HashtagRepository;


@Service
public class HashtagService {

	@Autowired
	private HashtagRepository hashtagRepository;
	@Autowired
    private HashtagOfCourseRepository hashTagOfCourseRepository;
	
	@Autowired
    private HashtagRepository hashTagRepository;

//    public List<String> getHashTagsByCourseId(int course) {
//        return hashTagOfCourseRepository.findHashTagsByCourseId(course);
//    }
	public List<HashTagOfCourse> getHashTagsByCourseId(int course) {
        return hashTagOfCourseRepository.findHashTagsByCourseId(course);
    }
    
	public List<HashTagOfCourse> getAllHashtag() {
		return hashTagOfCourseRepository.findAll();
	}
	
	public List<Course> getCoursesByHashTagId(int hashTagId) {
        return hashTagOfCourseRepository.findCoursesByHashTagId(hashTagId);
    }
}
