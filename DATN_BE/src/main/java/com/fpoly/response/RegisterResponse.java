package com.fpoly.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpoly.entity.User;

public class RegisterResponse {
	@JsonProperty("message")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public RegisterResponse(String message, User user) {
		super();
		this.message = message;
	}

}
