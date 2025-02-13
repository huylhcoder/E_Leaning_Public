package com.fpoly.security;

import com.fasterxml.jackson.annotation.JsonProperty;


import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

	@JsonProperty("message")
	private String message;

	@JsonProperty("token")
	private String token;

//	@JsonProperty("message")
//	private String message;
//
//	@JsonProperty("access_token")
//	private String accessToken;
//
//	@JsonProperty("refresh_token")
//	private String refreshToken;
//
//	// Private constructor to enforce the use of the builder
//	private LoginResponse(Builder builder) {
//		this.message = builder.message;
//		this.accessToken = builder.accessToken;
//		this.refreshToken = builder.refreshToken;
//	}
//
//	// Getters
//	public String getMessage() {
//		return message;
//	}
//
//	public String getAccessToken() {
//		return accessToken;
//	}
//
//	public String getRefreshToken() {
//		return refreshToken;
//	}
//
//	// Builder class
//	public static class Builder {
//		private String message;
//		private String accessToken;
//		private String refreshToken;
//
//		public Builder message(String message) {
//			this.message = message;
//			return this;
//		}
//
//		public Builder accessToken(String accessToken) {
//			this.accessToken = accessToken;
//			return this;
//		}
//
//		public Builder refreshToken(String refreshToken) {
//			this.refreshToken = refreshToken;
//			return this;
//		}
//
//		public LoginResponse build() {
//			return new LoginResponse(this);
//		}
//	}
}
