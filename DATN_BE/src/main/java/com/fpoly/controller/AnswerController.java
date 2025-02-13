package com.fpoly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fpoly.dto.AnswerDTO;
import com.fpoly.entity.Answer;
import com.fpoly.entity.Question;
import com.fpoly.service.AnswerService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // Lấy danh sách câu trả lời theo ID câu hỏi
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerDTO>> getAnswersByQuestionId(@PathVariable int questionId) {
        Question question = new Question();
        question.setQuestionId(questionId); // Giả sử bạn có phương thức để thiết lập ID câu hỏi

        List<Answer> answers = answerService.timListDapAnTheoCauHoiTam(question);
        
        // Chuyển đổi từ entity sang DTO
        List<AnswerDTO> answerDTOs = answers.stream().map(answer -> {
            AnswerDTO dto = new AnswerDTO();
            dto.setAnswerId(answer.getAnswerId());
            dto.setText(answer.getContent());
            dto.setCorrect(answer.isCorrect());
            return dto;
        }).toList();
        
        return ResponseEntity.ok(answerDTOs);
    }

    // Lưu câu trả lời mới
    @PostMapping
    public ResponseEntity<String> saveAnswer(@RequestBody AnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setAnswerId(answerDTO.getAnswerId());
        answer.setContent(answerDTO.getText());
        answer.setCorrect(answerDTO.isCorrect());

        // Lưu câu trả lời vào cơ sở dữ liệu
        answerService.saveAnswer(answer);
        
        return ResponseEntity.ok("Câu trả lời đã được lưu thành công.");
    }
}