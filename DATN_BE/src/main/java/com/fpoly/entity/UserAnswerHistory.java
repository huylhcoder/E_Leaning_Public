package com.fpoly.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "user_answer_history")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class UserAnswerHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_answer_history_id")
	private int userAnswerHistoryId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "users_id")
	User user;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "test_id")
	Test test;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "question_id")
	Question question;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "answer_id")
	Answer answer;

	@Column(name = "is_correct")
	private boolean isCorrect;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_ad")
	private Date createAt;

	public int getUserAnswerHistoryId() {
		return userAnswerHistoryId;
	}

	public void setUserAnswerHistoryId(int userAnswerHistoryId) {
		this.userAnswerHistoryId = userAnswerHistoryId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

}
