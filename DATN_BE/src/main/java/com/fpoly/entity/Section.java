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

@Entity
@Table(name = "section")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "section_id")
	private int sectionId;

	@ManyToOne
	@JoinColumn(name = "course_id")
	Course course;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "section_duration")
	private float sectionDuration;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@Temporal(TemporalType.DATE)
	@Column(name = "update_at")
	private Date updateAt;

	@JsonIgnore
	@OneToMany(mappedBy = "section")
	List<Lesson> listLesson;
	
	@JsonIgnore
	@OneToMany(mappedBy = "section")
	List<Material> listMaterial;
	
	@JsonIgnore
	@OneToMany(mappedBy = "section")
	List<Test> listTest;

	public int getSectionId() {
		return sectionId;
	}

	public Course getCourse() {
		return course;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public float getSectionDuration() {
		return sectionDuration;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public List<Lesson> getListLesson() {
		return listLesson;
	}

	public List<Material> getListMaterial() {
		return listMaterial;
	}

	public List<Test> getListTest() {
		return listTest;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSectionDuration(float sectionDuration) {
		this.sectionDuration = sectionDuration;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public void setListLesson(List<Lesson> listLesson) {
		this.listLesson = listLesson;
	}

	public void setListMaterial(List<Material> listMaterial) {
		this.listMaterial = listMaterial;
	}

	public void setListTest(List<Test> listTest) {
		this.listTest = listTest;
	}
	
}
