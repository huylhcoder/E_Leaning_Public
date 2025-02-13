package com.fpoly.dto;

import com.fpoly.entity.Comment;
import com.fpoly.entity.Reply;

public class ReplyCMTDTO {
	private Comment comment;
	private Reply reply;
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public Reply getReply() {
		return reply;
	}
	public void setReply(Reply reply) {
		this.reply = reply;
	}
	
	
}
