package com.fpoly.dto;

import java.util.List;

import com.fpoly.entity.Course;

public class PaymentDTO {
	private String tokenString;
	private int tienThanhToan;
	private List<Integer> ListCourseId;

	public String getTokenString() {
		return tokenString;
	}

	public int getTienThanhToan() {
		return tienThanhToan;
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}

	public void setTienThanhToan(int tienThanhToan) {
		this.tienThanhToan = tienThanhToan;
	}

	public List<Integer> getListCourseId() {
		return ListCourseId;
	}

	public void setListCourseId(List<Integer> listCourseId) {
		ListCourseId = listCourseId;
	}

}
