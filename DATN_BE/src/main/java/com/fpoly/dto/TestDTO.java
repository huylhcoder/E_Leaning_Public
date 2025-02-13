package com.fpoly.dto;

import java.util.List;

public class TestDTO {

	private int TestID;
	private String title;
	private int countdownTimer;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private List<QuestionDTO> listQuestion;

	public int getTestID() {
		return TestID;
	}

	public void setTestID(int testID) {
		TestID = testID;
	}

	public List<QuestionDTO> getListQuestion() {
		return listQuestion;
	}

	public void setListQuestion(List<QuestionDTO> listQuestion) {
		this.listQuestion = listQuestion;
	}

	public int getCountdownTimer() {
		return countdownTimer;
	}

	public void setCountdownTimer(int countdownTimer) {
		this.countdownTimer = countdownTimer;
	}
}
