package com.fpoly.dto;

import java.util.Date;

public class UserAnswerHistoryDTO {
    private int userAnswerHistoryId;
    private int questionId;
    private int answerId;
    private boolean isCorrect;
    private Date createAt;

    // Constructor
    public UserAnswerHistoryDTO(int userAnswerHistoryId, int questionId, int answerId, boolean isCorrect, Date createAt) {
        this.userAnswerHistoryId = userAnswerHistoryId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.isCorrect = isCorrect;
        this.createAt = createAt;
    }

	public int getUserAnswerHistoryId() {
		return userAnswerHistoryId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public int getAnswerId() {
		return answerId;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setUserAnswerHistoryId(int userAnswerHistoryId) {
		this.userAnswerHistoryId = userAnswerHistoryId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}   
}
