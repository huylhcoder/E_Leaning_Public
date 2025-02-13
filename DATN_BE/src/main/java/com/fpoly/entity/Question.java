package com.fpoly.entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "question")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_id")
	private int questionId;

	@ManyToOne
	@JoinColumn(name = "test_id")
	Test test;

	@Column(name = "contents")
	private String contents;

//	@JsonIgnore
//	@OneToMany(mappedBy = "question")
//	List<Answer> listAnswer;
	
//	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Answer> listAnswer = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Answer> listAnswer = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "question")
	List<UserAnswerHistory> listUserAnswerHistory;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public List<Answer> getListAnswer() {
		return listAnswer;
	}

	public void setListAnswer(List<Answer> listAnswer) {
		this.listAnswer = listAnswer;
	}

	public List<UserAnswerHistory> getListUserAnswerHistory() {
		return listUserAnswerHistory;
	}

	public void setListUserAnswerHistory(List<UserAnswerHistory> listUserAnswerHistory) {
		this.listUserAnswerHistory = listUserAnswerHistory;
	}
	
	

}
