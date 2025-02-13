package com.fpoly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_progress")
public class CourseProgress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_progress_id")
	private int courseProgressId;

	@ManyToOne
	@JoinColumn(name = "course_id")
	Course course;

	@ManyToOne
	@JoinColumn(name = "users_id")
	User user;
	
	@Column(name = "current_lession_id")
	private int currentLessionId;
	
	@Column(name = "total_lession")
	private int totalLession;
	
	@Column(name = "total_quiz")
	private int totalQuiz;
	
	@Column(name = "total_lession_complete")
	private int totalLessionComplete;
	
	@Column(name = "total_test_complete")
	private int totalTestComplete;
	
	@Column(name = "progress_percentage")
	private float progressPercentage;
	
	@Column(name = "progress_status")
	private int progressStatus;

	public int getCourseProgressId() {
		return courseProgressId;
	}

	public void setCourseProgressId(int courseProgressId) {
		this.courseProgressId = courseProgressId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getCurrentLessionId() {
		return currentLessionId;
	}

	public void setCurrentLessionId(int currentLessionId) {
		this.currentLessionId = currentLessionId;
	}

	public int getTotalLession() {
		return totalLession;
	}

	public void setTotalLession(int totalLession) {
		this.totalLession = totalLession;
	}

	public int getTotalQuiz() {
		return totalQuiz;
	}

	public void setTotalQuiz(int totalQuiz) {
		this.totalQuiz = totalQuiz;
	}

	public int getTotalLessionComplete() {
		return totalLessionComplete;
	}

	public void setTotalLessionComplete(int totalLessionComplete) {
		this.totalLessionComplete = totalLessionComplete;
	}

	public int getTotalTestComplete() {
		return totalTestComplete;
	}

	public void setTotalTestComplete(int totalTestComplete) {
		this.totalTestComplete = totalTestComplete;
	}

	public float getProgressPercentage() {
		return progressPercentage;
	}

	public void setProgressPercentage(float progressPercentage) {
		this.progressPercentage = progressPercentage;
	}

	public int getProgressStatus() {
		return progressStatus;
	}

	public void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}
	
	
	
}
