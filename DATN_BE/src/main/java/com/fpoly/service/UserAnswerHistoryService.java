package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Question;
import com.fpoly.entity.Test;
import com.fpoly.entity.User;
import com.fpoly.entity.UserAnswerHistory;
import com.fpoly.repository.UserAnswerHistoryRepository;

@Service
public class UserAnswerHistoryService {
	@Autowired
	private UserAnswerHistoryRepository repository;
	@Autowired
	private UserAnswerHistoryRepository userAnswerHistoryRepository;

	// Lưu đáp án người dùng đã chọn
//	public void saveOrUpdateAnswer(UserAnswerHistory history) {
//		if (history.getUser() == null || history.getQuestion() == null || history.getAnswer() == null) {
//			throw new IllegalArgumentException("Thiếu thông tin cần thiết!");
//		}
//
//		// Kiểm tra xem câu trả lời đã tồn tại chưa
//		UserAnswerHistory existingHistory = repository
//				.findByUserAndQuestion(history.getUser().getUserId(), history.getQuestion()).orElse(null);
//
//		if (existingHistory != null) {
//			// Cập nhật đáp án
//			existingHistory.setAnswer(history.getAnswer());
//			existingHistory.setCorrect(history.isCorrect());
//			repository.save(existingHistory);
//		} else {
//			// Lưu mới
//			repository.save(history);
//		}
//	}

	/**
	 * Lưu hoặc cập nhật lịch sử trả lời của người dùng.
	 * 
	 * @param userAnswerHistory đối tượng UserAnswerHistory cần lưu hoặc cập nhật.
	 * @return đối tượng UserAnswerHistory đã được lưu.
	 */
	public UserAnswerHistory saveOrUpdateAnswer(UserAnswerHistory userAnswerHistory) {
		return userAnswerHistoryRepository.save(userAnswerHistory);
	}

	/**
	 * Tìm kiếm lịch sử trả lời của người dùng theo ID người dùng và ID câu hỏi.
	 * 
	 * @param userId     ID của người dùng.
	 * @param questionId ID của câu hỏi.
	 * @return đối tượng UserAnswerHistory nếu tìm thấy, ngược lại trả về null.
	 */
	public UserAnswerHistory findByUserAndQuestion(User user, Question question) {
		return userAnswerHistoryRepository.findByUserAndQuestion(user, question);
	}

    public List<UserAnswerHistory> getAnswersByUserAndTest(User user, Test test) {
        return userAnswerHistoryRepository.findByUserAndTest(user, test);
    }

	public boolean deleteAnswersByUserAndTestId(User user, int testId) {
        try {
            userAnswerHistoryRepository.deleteByUserAndTestId(user.getUserId(), testId);
            return true;
        } catch (Exception e) {
            // Xử lý lỗi nếu cần
            return false;
        }
    }
}
