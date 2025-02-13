package com.fpoly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.entity.Category;
import com.fpoly.entity.Course;
import com.fpoly.entity.User;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	public Category findByCategoryId(int categoryId);
	

}
