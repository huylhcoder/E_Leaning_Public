package com.fpoly.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class UserRegisterDTO {
	@JsonProperty("fullname")
	private String fullname;

	@JsonProperty("email")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	private String password;

	@JsonProperty("retype_password")
	private String retypePassword;

	@JsonProperty("facebook_account_id")
	private int facebookAccountId;

	@JsonProperty("google_account_id")
	private int googleAccountId;

	public String getFullname() {
		return fullname;
	}

	public String getPassword() {
		return password;
	}

	public String getRetypePassword() {
		return retypePassword;
	}

	public int getFacebookAccountId() {
		return facebookAccountId;
	}

	public int getGoogleAccountId() {
		return googleAccountId;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}

	public void setFacebookAccountId(int facebookAccountId) {
		this.facebookAccountId = facebookAccountId;
	}

	public void setGoogleAccountId(int googleAccountId) {
		this.googleAccountId = googleAccountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
