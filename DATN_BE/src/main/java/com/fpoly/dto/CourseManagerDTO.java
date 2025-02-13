package com.fpoly.dto;

import java.util.Date;

public class CourseManagerDTO {
	private int courseId;
	private String avatar;
	private String name;
	private int status;// 0 => không công khai, 1 = công khai, 2 = không công khai
	private Date createAt;
	private int numberOfComment;// Số lượng comment
	private float revenue;// Doanh thu
	private float courseDuration;//Thời lượng

	public int getCourseId() {
		return courseId;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getName() {
		return name;
	}

	public int getStatus() {
		return status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public int getNumberOfComment() {
		return numberOfComment;
	}

	public float getRevenue() {
		return revenue;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void setNumberOfComment(int numberOfComment) {
		this.numberOfComment = numberOfComment;
	}

	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}

	public float getCourseDuration() {
		return courseDuration;
	}

	public void setCourseDuration(float courseDuration) {
		this.courseDuration = courseDuration;
	}

}
