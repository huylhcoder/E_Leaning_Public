package com.fpoly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.entity.FavoriteCourse;

@Repository
public interface FavoriteCourseRepository extends JpaRepository<FavoriteCourse, Integer> {
  
	//code của hào
	@Query("SELECT fc FROM FavoriteCourse fc JOIN fc.course ce WHERE fc.user.id = :userId ")
	List<FavoriteCourse> fillListCourseFcByUserID(int userId);
	
	FavoriteCourse  findByFavoriteCourseId(int id);
}
