package com.fpoly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fpoly.dto.MonthlyRevenueDTO;
import com.fpoly.entity.RegisteredCourse;
import com.fpoly.entity.Test;
import com.fpoly.entity.User;
import com.fpoly.entity.UserTestResult;

public interface UserTestResultRepository extends JpaRepository<UserTestResult, Integer> {
	
	// Hiển thị các test đã làm hào
	@Query("SELECT ur FROM UserTestResult ur JOIN ur.test t WHERE ur.user.id = :userId")
	List<UserTestResult> findByUser(@Param("userId") int userId);
	//

	UserTestResult findByUserAndTest(User user, Test test);
}
