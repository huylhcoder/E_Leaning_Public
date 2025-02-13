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
@Table(name = "course")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_id")
	private int courseId;

	@ManyToOne
	@JoinColumn(name = "category_id")
	Category category;

	@ManyToOne
	@JoinColumn(name = "users_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "course_level_id")
	CourseLevel courseLevel;

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "name")
	private String name;

	@Column(name = "topic")
	private String topic;

	@Column(name = "description")
	private String description;

	@Column(name = "path_video_demo")
	private String pathVideoDemo;

	@Column(name = "follow")
	private int follow;

	@Column(name = "total_rate")
	private float totalRate;

	@Column(name = "average_rating")
	private float averageRating;

	@Column(name = "number_of_lesson")
	private int numberOfLesson;

	@Column(name = "course_duration")
	private float courseDuration;

	// 0 => nháp, 1 => công khai, 2 => không công khai
	@Column(name = "status")
	private int status;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@Temporal(TemporalType.DATE)
	@Column(name = "update_at")
	private Date updateAt;

	@Column(name = "price")
	private float price;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	List<HashTagOfCourse> listHashTagOfCourse;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	List<Section> listSection;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	List<Cart> listcart;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	List<Comment> listComment;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	List<CourseProgress> listCourseProgress;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	List<FavoriteCourse> listFavouriteCourse;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	List<RegisteredCourse> listRegisteredCourse;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CourseLevel getCourseLevel() {
		return courseLevel;
	}

	public void setCourseLevel(CourseLevel courseLevel) {
		this.courseLevel = courseLevel;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPathVideoDemo() {
		return pathVideoDemo;
	}

	public void setPathVideoDemo(String pathVideoDemo) {
		this.pathVideoDemo = pathVideoDemo;
	}

	public int getFollow() {
		return follow;
	}

	public void setFollow(int follow) {
		this.follow = follow;
	}

	public float getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(float totalRate) {
		this.totalRate = totalRate;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public int getNumberOfLesson() {
		return numberOfLesson;
	}

	public void setNumberOfLesson(int numberOfLesson) {
		this.numberOfLesson = numberOfLesson;
	}

	public float getCourseDuration() {
		return courseDuration;
	}

	public void setCourseDuration(float courseDuration) {
		this.courseDuration = courseDuration;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Section> getListSection() {
		return listSection;
	}

	public void setListSection(List<Section> listSection) {
		this.listSection = listSection;
	}

	public List<Cart> getListcart() {
		return listcart;
	}

	public void setListcart(List<Cart> listcart) {
		this.listcart = listcart;
	}

	public List<Comment> getListComment() {
		return listComment;
	}

	public void setListComment(List<Comment> listComment) {
		this.listComment = listComment;
	}

	public List<CourseProgress> getListCourseProgress() {
		return listCourseProgress;
	}

	public void setListCourseProgress(List<CourseProgress> listCourseProgress) {
		this.listCourseProgress = listCourseProgress;
	}

	public List<FavoriteCourse> getListFavouriteCourse() {
		return listFavouriteCourse;
	}

	public void setListFavouriteCourse(List<FavoriteCourse> listFavouriteCourse) {
		this.listFavouriteCourse = listFavouriteCourse;
	}

	public List<RegisteredCourse> getListRegisteredCourse() {
		return listRegisteredCourse;
	}

	public void setListRegisteredCourse(List<RegisteredCourse> listRegisteredCourse) {
		this.listRegisteredCourse = listRegisteredCourse;
	}

}
