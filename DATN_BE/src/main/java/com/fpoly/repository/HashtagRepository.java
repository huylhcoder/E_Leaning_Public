package com.fpoly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.entity.HashTag;
import com.fpoly.entity.HashTagOfCourse;

@Repository
public interface HashtagRepository extends JpaRepository<HashTag, Integer> {

	@Query("SELECT ht FROM HashTag ht " +
	           "JOIN HashTagOfCourse htc ON ht.hashTagId = htc.hashTag.hashTagId " +
	           "WHERE htc.course.courseId = :courseId")
	    List<HashTag> findHashTagsByCourseId(@Param("courseId") int courseId);
	
}
