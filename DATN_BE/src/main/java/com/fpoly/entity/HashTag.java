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
@Table(name = "hash_tag")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class HashTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hash_tag_id")
	private int hashTagId;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "hashTag")
	List<HashTagOfCourse> listHashTagOfCourse;

	public int getHashTagId() {
		return hashTagId;
	}

	public String getName() {
		return name;
	}

	public List<HashTagOfCourse> getListHashTagOfCourse() {
		return listHashTagOfCourse;
	}

	public void setHashTagId(int hashTagId) {
		this.hashTagId = hashTagId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setListHashTagOfCourse(List<HashTagOfCourse> listHashTagOfCourse) {
		this.listHashTagOfCourse = listHashTagOfCourse;
	}

}
