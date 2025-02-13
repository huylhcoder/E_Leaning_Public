package com.fpoly.entity;

import java.util.Date;
import java.util.List;

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

@Entity
@Table(name = "lesson")
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lesson_id")
	private int lessonId;
	
	@ManyToOne
	@JoinColumn(name = "section_id")
	Section section;
	
	@Column(name = "lesson_duration")
	private float lessionDuration;
	
	@Column(name = "path_video")
	private String pathVideo;

	@Column(name = "description")
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@Temporal(TemporalType.DATE)
	@Column(name = "update_at")
	private Date updateAt;
	
	@Column(name = "title")
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "lesson")
	List<LessonComplete> listLessonComplete;

	public int getLessonId() {
		return lessonId;
	}

	public Section getSection() {
		return section;
	}

	public float getLessionDuration() {
		return lessionDuration;
	}

	public String getPathVideo() {
		return pathVideo;
	}

	public String getDescription() {
		return description;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public String getName() {
		return name;
	}

	public List<LessonComplete> getListLessonComplete() {
		return listLessonComplete;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public void setLessionDuration(float lessionDuration) {
		this.lessionDuration = lessionDuration;
	}

	public void setPathVideo(String pathVideo) {
		this.pathVideo = pathVideo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setListLessonComplete(List<LessonComplete> listLessonComplete) {
		this.listLessonComplete = listLessonComplete;
	}
	
}
