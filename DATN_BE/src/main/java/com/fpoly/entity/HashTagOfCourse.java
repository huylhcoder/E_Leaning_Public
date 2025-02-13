package com.fpoly.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table(name = "hash_tag_of_course")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class HashTagOfCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hash_tag_of_course_id")
	private int hashTagId;

	@ManyToOne
	@JoinColumn(name = "course_id")
	Course course;

	@ManyToOne
	@JoinColumn(name = "hash_tag_id")
	HashTag hashTag;

	public int getHashTagId() {
		return hashTagId;
	}

	public Course getCourse() {
		return course;
	}

	public HashTag getHashTag() {
		return hashTag;
	}

	public void setHashTagId(int hashTagId) {
		this.hashTagId = hashTagId;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setHashTag(HashTag hashTag) {
		this.hashTag = hashTag;
	}

}
