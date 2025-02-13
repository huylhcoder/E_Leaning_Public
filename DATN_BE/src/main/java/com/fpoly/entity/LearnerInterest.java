package com.fpoly.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "learner_interest")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class LearnerInterest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "learner_interest_id")
	private int learnerGoalId;

	@ManyToOne
	@JoinColumn(name = "users_id")
	User user;

	@Column(name = "interest_name")
	private String interestName;

	public int getLearnerGoalId() {
		return learnerGoalId;
	}

	public void setLearnerGoalId(int learnerGoalId) {
		this.learnerGoalId = learnerGoalId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getInterestName() {
		return interestName;
	}

	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	
}
