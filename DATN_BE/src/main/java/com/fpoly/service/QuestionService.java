package com.fpoly.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpoly.entity.Answer;
import com.fpoly.entity.Question;
import com.fpoly.entity.Test;
import com.fpoly.exceptions.ResourceNotFoundException;
import com.fpoly.repository.QuestionRepository;

@Service
public class QuestionService {
	@Autowired
	QuestionRepository questionRepository;

	public List<Question> timKiemDanhSachCauHoiTheoBaiQuizTam(Test test) {
		return questionRepository.findByTest(test);
	}
	
	public Optional<Question> timKiemCauHoiTheoIdCauHoi_Tam(int questionId) {
		return questionRepository.findById(questionId);
	}

	// Tìm kiếm câu hỏi theo Id
	public Optional<Question> timKiemCauHoiTheoIdCauHoi_Huy(int questionId) {
		return questionRepository.findById(questionId);
	}

	public Question updateQuestion(String questionId, Question question) {
		// TODO Auto-generated method stub
		return null;
	}

	// Cập nhật nội dung câu hỏi
	public Question updateContentsQuestion(int questionId, Question updatedQuestion) {
	    // Tìm câu hỏi theo ID
	    Question existingQuestion = questionRepository.findById(questionId)
	            .orElseThrow(() -> new ResourceNotFoundException("Question not found"));	    
	    // Cập nhật nội dung câu hỏi
	    existingQuestion.setContents(updatedQuestion.getContents());
	    
	    return questionRepository.save(existingQuestion);
	}
	
	//Cập nhật đáp án
	public Question save(Question question) {
        return questionRepository.save(question);
    }

	//Xóa câu hỏi
	public void xoaCauHoi(Question questionCanDuocXoa) {
		questionRepository.delete(questionCanDuocXoa);		
	}

	//Thêm câu hỏi mới
	public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

	public void saveAll(List<Question> questions) {
        questionRepository.saveAll(questions); // Lưu tất cả các câu hỏi vào DB
    }

}
