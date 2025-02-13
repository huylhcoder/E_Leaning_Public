package com.fpoly.dto;

import java.util.List;

public class SectionCourseDetailDTO {
	private int sectionId;
	private String sectionName;
	List<LessonCourseDetailDTO> listLesson;
	List<TestDTO> listTest;
	
	
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public List<LessonCourseDetailDTO> getListLesson() {
		return listLesson;
	}
	public void setListLesson(List<LessonCourseDetailDTO> listLesson) {
		this.listLesson = listLesson;
	}
	public List<TestDTO> getListTest() {
		return listTest;
	}
	public void setListTest(List<TestDTO> listTest) {
		this.listTest = listTest;
	}
	
	
}
