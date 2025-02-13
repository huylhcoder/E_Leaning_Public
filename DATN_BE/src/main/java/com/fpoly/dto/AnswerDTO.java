package com.fpoly.dto;


public class AnswerDTO {

	private int answerId;
	private String text;
	private boolean isCorrect; // Thêm thuộc tính này

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	// Getters và Setters cho isCorrect
    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

}
