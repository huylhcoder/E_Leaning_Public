package com.fpoly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Category;
import com.fpoly.entity.Course;
import com.fpoly.entity.CourseLevel;
import com.fpoly.repository.CourseLevelRepository;
import com.fpoly.repository.CourseRepository;

@Service
public class CourseLevelService {
	@Autowired
	CourseLevelRepository courseLevelRepository;

	public List<CourseLevel> hienThiTatCaLevel() {
		return courseLevelRepository.findAll();
	}

	public CourseLevel timKiemChiTietLevelTheoId(int levelId) {
		return courseLevelRepository.findById(levelId).orElseThrow(null);
	}

}
