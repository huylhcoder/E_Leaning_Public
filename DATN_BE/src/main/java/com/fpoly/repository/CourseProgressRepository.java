package com.fpoly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.entity.Answer;
import com.fpoly.entity.Course;
import com.fpoly.entity.CourseProgress;
import com.fpoly.entity.User;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Integer> {
	@Query("SELECT cp FROM CourseProgress cp JOIN cp.course ce JOIN cp.user u WHERE	 u.userId = :userId")
    List<CourseProgress> FillCourseKhoa(int userId);
	
	CourseProgress findByCourseProgressId(int id);
	
	// Code của HBao
	@Query("SELECT c FROM CourseProgress c WHERE c.progressPercentage = 100")
	List<CourseProgress> FillTotalCourseComplete();
	
	@Query("SELECT c FROM CourseProgress c WHERE c.progressPercentage = 100 AND c.progressStatus = 1")
	List<CourseProgress> FillTotalCourseCompleteCuaBao();
	//

	CourseProgress findByUserAndCourse(User user, Course course);
	
	//
	@Query("SELECT cp FROM CourseProgress cp WHERE cp.user.userId = :userId AND cp.course.courseId = :courseId")
	Optional<CourseProgress> findByCourseId(@Param("userId") int userId, @Param("courseId") int courseId);

	 Optional<CourseProgress> findByCourse_CourseIdAndUser_UserId(int courseId, int userId);
}
