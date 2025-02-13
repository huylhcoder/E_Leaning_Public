package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Comment;
import com.fpoly.entity.Course;
import com.fpoly.entity.Reply;
import com.fpoly.entity.User;
import com.fpoly.repository.AnswerRepository;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.ReplyRepository;

@Service
public class ReplyService {
	@Autowired
    ReplyRepository replyRepository;
	
	public List<Reply> getAllReply() {
		return replyRepository.findAll();
	}
	
	public List<Reply> getRepliesByCommentId(int commentId) {
	    return replyRepository.findByCommentId(commentId);
	}
	
	public Reply getRepliesByCommentIdToan(Comment comment) {
	    return replyRepository.findByComment(comment);
	}
	
	public Reply addReply(Reply rep) {
        return replyRepository.save(rep); // Lưu reply vào cơ sở dữ liệu
    }

}
