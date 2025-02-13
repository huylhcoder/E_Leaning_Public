package com.fpoly.dto;

import java.util.List;

public class QuestionDTO {

	private int questionId;
	private String contents;
	List<AnswerDTO> listAnswerDTO;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public List<AnswerDTO> getListAnswerDTO() {
		return listAnswerDTO;
	}

	public void setListAnswerDTO(List<AnswerDTO> listAnswerDTO) {
		this.listAnswerDTO = listAnswerDTO;
	}

}
