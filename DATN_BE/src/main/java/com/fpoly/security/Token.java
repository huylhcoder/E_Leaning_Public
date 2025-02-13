package com.fpoly.security;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fpoly.entity.User;

@Entity
@Table(name = "token")
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "token", length = 255)
	private String token;

	@Column(name = "token_type", length = 50)
	private String tokenType;

	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	private boolean revoked;
	private boolean expired;

	@OneToOne
	@JoinColumn(name = "users_id", referencedColumnName = "users_id")
	private User user;

	// Private constructor to enforce use of the builder
	private Token(Builder builder) {
		this.id = builder.id;
		this.token = builder.token;
		this.expirationDate = builder.expirationDate;
		this.revoked = builder.revoked;
		this.expired = builder.expired;
		this.user = builder.user;
	}

	// Builder class
	public static class Builder {
		private Long id;
		private String token;
		private LocalDateTime expirationDate;
		private boolean revoked;
		private boolean expired;
		private User user;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder token(String token) {
			this.token = token;
			return this;
		}

		public Builder expirationDate(LocalDateTime date) {
			this.expirationDate = date;
			return this;
		}

		public Builder revoked(boolean revoked) {
			this.revoked = revoked;
			return this;
		}

		public Builder expired(boolean expired) {
			this.expired = expired;
			return this;
		}

		public Builder user(User user) {
			this.user = user;
			return this;
		}

		public Token build() {
			return new Token(this);
		}
	}

	// Getters for the fields
	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public boolean isExpired() {
		return expired;
	}

	public User getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
