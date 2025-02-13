package com.fpoly.service;

import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Course;
import com.fpoly.entity.CourseProgress;
import com.fpoly.entity.RegisteredCourse;
import com.fpoly.entity.Test;
import com.fpoly.entity.User;
import com.fpoly.entity.UserTestResult;
import com.fpoly.entity.Voucher;
import com.fpoly.repository.AnswerRepository;
import com.fpoly.repository.CourseProgressRepository;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.RegisteredCourseRepository;
import com.fpoly.repository.UserRepository;
import com.fpoly.repository.UserTestResultRepository;

@Service
public class UserTestResultService {
	@Autowired
	private UserTestResultRepository userTestResult;
	@Autowired
    private UserTestResultRepository userTestResultRepository;
	
	public List<UserTestResult>findUserTestResultByUserHao( int userId){
		return userTestResult.findByUser(userId);
		}

	public UserTestResult findByUserAndTest(User user, Test test) {
        return userTestResultRepository.findByUserAndTest(user, test);
    }

	public UserTestResult save(UserTestResult userTestResult) {
        return userTestResultRepository.save(userTestResult);
    }
}

