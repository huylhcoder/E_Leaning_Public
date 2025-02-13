package com.fpoly.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpoly.entity.Answer;
import com.fpoly.entity.Question;
import com.fpoly.exceptions.ResourceNotFoundException;
import com.fpoly.repository.AnswerRepository;

import java.util.List;

@Service
public class AnswerService {
    
    @Autowired
    AnswerRepository answerRepository;

    public List<Answer> timListDapAnTheoCauHoiTam(Question question) {
        return answerRepository.findByQuestion(question);
    }

    // Phương thức để lưu câu trả lời
    public void saveAnswer(Answer answer) {
        answerRepository.save(answer);
    }

	public Answer getById(Integer answerId) {
		// TODO Auto-generated method stub
		return answerRepository.getById(answerId);
	}

	// Thêm đáp án
	public Answer themDapAn(Answer dapAn) {
		return answerRepository.save(dapAn);
	}

	// Tìm kiếm đáp án theo
	public Optional<Answer> timKiemDapAnTheoId(int answerId) {
		return answerRepository.findById(answerId);
	}

	// Xóa đáp án
	public void xoaDapAn(Answer dapAn) {
		answerRepository.delete(dapAn);
	}

	// Cập nhật nội dung đáp án
	public Answer updateAnswer(int answerId, Answer updatedAnswer) {
		// Tìm đáp án theo ID
		Answer existingAnswer = answerRepository.findById(answerId)
				.orElseThrow(() -> new ResourceNotFoundException("Answer not found"));
		// Cập nhật nội dung và trạng thái đúng/sai
		existingAnswer.setContent(updatedAnswer.getContent());
		existingAnswer.setCorrect(updatedAnswer.isCorrect());

		return answerRepository.save(existingAnswer);
	}

	// Cập nhật đáp án đúng
	public Answer save(Answer answer) {
		return answerRepository.save(answer);
	}

	//
	public void xoaCauTraLoi(int answerId) {
	    Answer answer = answerRepository.findById(answerId).orElse(null);
	    if (answer != null) {
	        answerRepository.delete(answer);
	    }
	}

	public void saveAll(List<Answer> answers) {
		 answerRepository.saveAll(answers);
	}
}
