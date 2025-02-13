package com.fpoly.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.dto.UserAnswerDto;
import com.fpoly.dto.UserAnswerHistoryDTO;
import com.fpoly.entity.Answer;
import com.fpoly.entity.Question;
import com.fpoly.entity.Test;
import com.fpoly.entity.User;
import com.fpoly.entity.UserAnswerHistory;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.service.AnswerService;
import com.fpoly.service.QuestionService;
import com.fpoly.service.TestService;
import com.fpoly.service.UserAnswerHistoryService;
import com.fpoly.service.UserService;

@RestController
@RequestMapping("${api.prefix}/user_answer_history")
public class UserAnswerHistoryController {

	@Autowired
	private UserAnswerHistoryService userAnswerHistoryService;
	@Autowired
	private UserService usService;
	@Autowired
	private TestService testService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private AnswerService answerService;

	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	@PostMapping("/save-answer")
	public ResponseEntity<?> saveOrUpdateAnswer(@RequestHeader("Authorization") Optional<String> token,
			@RequestParam("maBaiKiemTra") Optional<Integer> testId,
			@RequestParam("maCauHoi") Optional<Integer> questionId,
			@RequestParam("maDapAn") Optional<Integer> answerId) {

		// Lấy token và loại bỏ "Bearer "
		String tokenValue = token.map(t -> t.replace("Bearer ", "")).orElse("");
		System.out.println("Token: " + tokenValue);
		System.out.println("Question ID: " + questionId.orElse(null));
		System.out.println("Answer ID: " + answerId.orElse(null));

		// Tìm kiếm email từ token
		String email;
		try {
			email = jwtTokenUtils.extractEmail(tokenValue);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("{\"message\": \"Token không hợp lệ!\"}");
		}

		// Tìm kiếm người dùng theo email
		User user = usService.getUserByEmailToan(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("{\"message\": \"Email không tồn tại!\"}");
		}

		System.out.println("REST Save Đáp án - Qua đoạn kiểm tra user");

		// Tìm kiếm bài kiểm tra theo id
		Test test = testService.timTestTheoIdTam(testId.orElse(0));

		// Tìm kiếm câu hỏi theo ID
		Question question = questionService.timKiemCauHoiTheoIdCauHoi_Tam(questionId.orElse(0)).orElse(null);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("{\"message\": \"Câu hỏi không tồn tại!\"}");
		}

		// Tìm kiếm lịch sử đã chọn theo câu hỏi và đáp án của người dùng
		UserAnswerHistory existingHistory = userAnswerHistoryService.findByUserAndQuestion(user, question);

		try {
			Answer answer = answerService.getById(answerId.orElse(0));
			if (answer == null) {
				return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("{\"message\": \"Đáp án không tồn tại!\"}");
			}

			if (existingHistory != null) {
				// Nếu đã có lịch sử, cập nhật đáp án
				existingHistory.setAnswer(answer);
				existingHistory.setCorrect(answer.isCorrect());
				userAnswerHistoryService.saveOrUpdateAnswer(existingHistory);
				return ResponseEntity.ok(existingHistory);
			} else {
				// Nếu chưa có lịch sử, thêm mới
				UserAnswerHistory newHistory = new UserAnswerHistory();
				newHistory.setUser(user);
				newHistory.setTest(test);
				newHistory.setQuestion(question);
				newHistory.setAnswer(answer);
				newHistory.setCorrect(answer.isCorrect());
				newHistory.setCreateAt(new Date()); // Ghi lại thời gian tạo
				userAnswerHistoryService.saveOrUpdateAnswer(newHistory);
				return ResponseEntity.ok(newHistory);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Lỗi khi lưu/cập nhật câu trả lời.");
		}
	}

	// Lấy danh sách đáp án người dùng đã chọn
	@GetMapping("/get-answers/{testId}")
	public ResponseEntity<?> getAnswers(@RequestHeader("Authorization") Optional<String> token,
			@PathVariable("testId") int testId) {
		// Lấy token và loại bỏ "Bearer "
		String tokenValue = token.map(t -> t.replace("Bearer ", "")).orElse("");
		System.out.println("Token: " + tokenValue);

		// Tìm kiếm email từ token
		String email;
		try {
			email = jwtTokenUtils.extractEmail(tokenValue);
			System.out.println("Lịch sử làm bài - Email: " + email);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("{\"message\": \"Token không hợp lệ!\"}");
		}

		// Tìm kiếm người dùng theo email
		User user = usService.getUserByEmailToan(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("{\"message\": \"Email không tồn tại!\"}");
		}

		// Tìm kiếm bài kiểm tra theo id
		Test test = testService.timTestTheoIdTam(testId);

		// Lấy lịch sử đáp án đã chọn
		List<UserAnswerHistory> answers = userAnswerHistoryService.getAnswersByUserAndTest(user, test);
	    // Chuyển đổi sang DTO
	    List<UserAnswerHistoryDTO> answerDTOs = answers.stream().map(answer -> 
	        new UserAnswerHistoryDTO(
	            answer.getUserAnswerHistoryId(),
	            answer.getQuestion().getQuestionId(),
	            answer.getAnswer().getAnswerId(),
	            answer.isCorrect(),
	            answer.getCreateAt()
	        )
	    ).collect(Collectors.toList());

	    return ResponseEntity.ok(answerDTOs);
	}

}