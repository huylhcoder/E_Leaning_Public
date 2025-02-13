package com.fpoly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.entity.Category;
import com.fpoly.service.CategoryService;

@CrossOrigin("*") // cho phép bên ngoài truy xuất vào thoải mái k ngăn cản gì cả
@RestController
@RequestMapping("${api.prefix}/tamCategory")
public class tamCategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public List<Category> getListTam() {
		return categoryService.getListTam();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Category> timkiemDanhMucTheoIDTam(@PathVariable("id") int id) {
	    try {
	        Category danhMuc = categoryService.timKiemDanhMucTheoIDTam(id);
	        if (danhMuc == null) {
	            throw new RuntimeException("Không tìm thấy danh mục");
	        }
	        return ResponseEntity.ok(danhMuc);
	    } catch (RuntimeException e) {
	        return ResponseEntity.notFound().build();
	    }
	}

	@PostMapping
	public ResponseEntity<Category> themDanhMucTam(@RequestBody Category a) {
		try {
			categoryService.themDanhMucTam(a);
			return ResponseEntity.ok(a);
		} catch (Exception e) {
			return ResponseEntity.ok(a);
		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<Category> capNhatDanhMucTam(@PathVariable("id") int id, @RequestBody Category a) {
		Category kiemTraTonTai = categoryService.timKiemDanhMucTheoIDTam(id);
		if (kiemTraTonTai != null) {
			a.setCategoryId(id);
			categoryService.capNhatDanhMucTam(a);
			return ResponseEntity.ok(a);
		}
		return ResponseEntity.ok(a);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Category> xoaDanhMucTam(@PathVariable("id") int id) {
		Category kiemTraTonTai = categoryService.timKiemDanhMucTheoIDTam(id);
		if (kiemTraTonTai != null) {
			categoryService.xoaDanhMucTheoIDTam(id);
			return ResponseEntity.ok(kiemTraTonTai);
		}
		return ResponseEntity.ok(kiemTraTonTai);
	}
	
	
	
}
