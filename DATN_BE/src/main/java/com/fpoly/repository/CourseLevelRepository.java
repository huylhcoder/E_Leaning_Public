package com.fpoly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.entity.CourseLevel;

@Repository
public interface CourseLevelRepository extends JpaRepository<CourseLevel, Integer> {
	
}
