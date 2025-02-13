package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Category;
import com.fpoly.entity.Course;
import com.fpoly.repository.AnswerRepository;
import com.fpoly.repository.CategoryRepository;
import com.fpoly.repository.CourseRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	

	public List<Category> getListTam() {
		return categoryRepository.findAll();
	}
	
	public Category themDanhMucTam(Category a) {
		return categoryRepository.save(a);
	}
	
	public Category capNhatDanhMucTam(Category a) {
		return categoryRepository.save(a);
	}
	
	public Category timKiemDanhMucTheoIDTam(int id) {
		return categoryRepository.findByCategoryId(id);
	}
	
	public void xoaDanhMucTheoIDTam(int id) {
		 categoryRepository.deleteById(id);
	}
	
  
	
}
