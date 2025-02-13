package com.fpoly.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "test")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Test {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "test_id")
	private int testId;

	@ManyToOne
	@JoinColumn(name = "section_id")
	Section section;

	@Column(name = "title")
	private String title;

	@Column(name = "number_of_question")
	private String numberOfQuestion;

	@Column(name = "description")
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@Temporal(TemporalType.DATE)
	@Column(name = "update_at")
	private Date updateAt;
	
	//Đồng hồ điểm ngược tính theo giây
	@Column(name = "countdown_timer")
	private int countdownTimer;

	@JsonIgnore
	@OneToMany(mappedBy = "test")
	List<UserTestResult> listUserTestResult;
	 	
	@JsonIgnore
	@OneToMany(mappedBy = "test")
	List<Question> listQuestion;
	
	@JsonIgnore
	@OneToMany(mappedBy = "test")
	List<UserAnswerHistory> listUserAnswerHistory;

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNumberOfQuestion() {
		return numberOfQuestion;
	}

	public void setNumberOfQuestion(String numberOfQuestion) {
		this.numberOfQuestion = numberOfQuestion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<UserTestResult> getListUserTestResult() {
		return listUserTestResult;
	}

	public void setListUserTestResult(List<UserTestResult> listUserTestResult) {
		this.listUserTestResult = listUserTestResult;
	}

	public List<Question> getListQuestion() {
		return listQuestion;
	}

	public void setListQuestion(List<Question> listQuestion) {
		this.listQuestion = listQuestion;
	}

	public int getCountdownTimer() {
		return countdownTimer;
	}

	public List<UserAnswerHistory> getListUserAnswerHistory() {
		return listUserAnswerHistory;
	}

	public void setCountdownTimer(int countdownTimer) {
		this.countdownTimer = countdownTimer;
	}

	public void setListUserAnswerHistory(List<UserAnswerHistory> listUserAnswerHistory) {
		this.listUserAnswerHistory = listUserAnswerHistory;
	}

}
