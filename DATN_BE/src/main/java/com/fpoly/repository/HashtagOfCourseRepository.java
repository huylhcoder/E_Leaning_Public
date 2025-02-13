package com.fpoly.repository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.entity.Cart;
import com.fpoly.entity.Course;
import com.fpoly.entity.HashTag;
import com.fpoly.entity.HashTagOfCourse;

import jakarta.persistence.Tuple;


@Repository
public interface HashtagOfCourseRepository extends JpaRepository<HashTagOfCourse, Integer> {
	

	
//	@Query("SELECT h.hashTag.name FROM HashTagOfCourse h WHERE h.course.courseId = :courseId")
//	List<String> findHashTagsByCourseId(@Param("courseId") int courseId);
	
//	@Query("SELECT h.hashTag.name FROM HashTagOfCourse h WHERE h.course.courseId = :courseId")
//    List<String> findHashTagsByCourseId(@Param("courseId") int courseId);
	
	@Query("SELECT h FROM HashTagOfCourse h WHERE h.course.courseId = :courseId")
    List<HashTagOfCourse> findHashTagsByCourseId(@Param("courseId") int courseId);
	
	@Query("SELECT htc.course FROM HashTagOfCourse htc WHERE htc.hashTag.hashTagId = :hashTagId")
    List<Course> findCoursesByHashTagId(@Param("hashTagId") int hashTagId);

}
