package com.fpoly.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.dto.ReplyCMTDTO;
import com.fpoly.dto.ReplyDTO;
import com.fpoly.entity.Comment;
import com.fpoly.entity.Course;
import com.fpoly.entity.Reply;
import com.fpoly.entity.User;
import com.fpoly.entity.Voucher;
import com.fpoly.repository.ReplyRepository;
import com.fpoly.service.CommentService;
import com.fpoly.service.CourseService;
import com.fpoly.service.ReplyService;
import com.fpoly.service.UserService;

@CrossOrigin("*") // cho phép bên ngoài truy xuất vào thoải mái k ngăn cản gì cả
@RestController
@RequestMapping("/api/reply")
public class ReplyController {
	@Autowired ReplyService rs;
	@Autowired ReplyRepository rp;
	@Autowired
	UserService userService;
	@Autowired
	CommentService cmtService;
	@Autowired
	CourseService courseService;
	@GetMapping("")
	public List<Reply> getAllReply() {
		return rs.getAllReply();
	}
	
	@GetMapping("/courseId/{courseId}")
	public List<ReplyCMTDTO> getRepliesByCommentId(@PathVariable("courseId") int courseId) {
		Course course = courseService.timKhoaHocTheoMaKhoaHocToan(courseId);
		List<ReplyCMTDTO> listRepDTO = new ArrayList<ReplyCMTDTO>();
		List<Comment> listCMT = cmtService.hienThiDanhGiaTheoKhoaHoc(courseId);
		for (Comment comment : listCMT) {
			Reply rep = rs.getRepliesByCommentIdToan(comment);
			//
			ReplyCMTDTO repDTO = new ReplyCMTDTO();
			repDTO.setComment(comment);
			repDTO.setReply(rep);
			//
			listRepDTO.add(repDTO);
		}
	    return listRepDTO;
	    
	}
	

	
	
	@PostMapping("/{commentId}")
	public ResponseEntity<ReplyDTO> addReply(@RequestBody ReplyDTO a) {
		try {
			User user = userService.getUserById(a.getUserId());
			Comment comment = cmtService.getCommentById(a.getCommentId());
			Reply rep = new Reply();
			rep.setUser(user);
			rep.setContent(a.getContent());
			rep.setComment(comment);
			rep.setCreateAt(new Date());
			rs.addReply(rep);
			return ResponseEntity.ok(a);
		} catch (Exception e) {
			return ResponseEntity.ok(a);
		}
	}
}
