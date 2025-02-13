package com.fpoly.dto;


public class ReplyDTO {
	private String content;
	private int commentId;
	private int userId;
	private boolean replyStatus;
	public boolean isReplyStatus() {
		return replyStatus;
	}
	public void setReplyStatus(boolean replyStatus) {
		this.replyStatus = replyStatus;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	

}
