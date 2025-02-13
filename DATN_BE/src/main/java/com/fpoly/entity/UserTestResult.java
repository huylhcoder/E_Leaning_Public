package com.fpoly.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "user_test_result")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class UserTestResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_test_result_id")
	private int userTestResultId;

	@ManyToOne
	@JoinColumn(name = "users_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "test_id")
	Test test;

	@Column(name = "score")
	private float score;

	@Column(name = "completion_time")
	private float completionTime;

	@Column(name = "number_of_correct_answer")
	private int numberOfCorrectAnswer;

	@Column(name = "status")
	private boolean status;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@Temporal(TemporalType.DATE)
	@Column(name = "update_at")
	private Date updateAt;
	
	@Column(name = "max_score")
	private float maxScore;

	public int getUserTestResultId() {
		return userTestResultId;
	}

	public void setUserTestResultId(int userTestResultId) {
		this.userTestResultId = userTestResultId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(float completionTime) {
		this.completionTime = completionTime;
	}

	public int getNumberOfCorrectAnswer() {
		return numberOfCorrectAnswer;
	}

	public void setNumberOfCorrectAnswer(int numberOfCorrectAnswer) {
		this.numberOfCorrectAnswer = numberOfCorrectAnswer;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public float getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(float maxScore) {
		this.maxScore = maxScore;
	}
	
}
