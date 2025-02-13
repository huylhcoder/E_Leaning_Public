package com.fpoly.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.fpoly.security.BaseEntity;

@Entity
@Table(name = "users")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class User extends BaseEntity implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "users_id")
	private int userId;

	@ManyToOne
	@JoinColumn(name = "role_id")
	Role role;

	@Column(name = "password")
	private String password;

	@Column(name = "name")
	private String name;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "url_profile_image")
	private String urlProfileImage;

	@Column(name = "is_active")
	private boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at")
	private Date createAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_at")
	private Date updateAt;

	@Column(name = "facebook_account_id")
	private int facebookAccountId;

	@Column(name = "google_account_id")
	private int gooogleAccountId;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<SocialAccount> listSocialAccount;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<LearnerInterest> listLearnerInterest;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<LearnerSkill> listLearnerSkill;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<LearnerLevel> listLearnerLevel;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<LearnerGoal> listLearnerGoal;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Voucher> listVoucher;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Payment> listPayment;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<RegisteredCourse> listRegisteredCourse;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Cart> listCart;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<CourseProgress> listCourseProgress;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Course> listCourse;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<FavoriteCourse> listFavoriteCourse;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Comment> listComment;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Reply> listReply;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<LessonComplete> listLessonComplte;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<UserTestResult> UserTestResult;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<UserAnswerHistory> listUserAnswerHistory;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<MyVoucher> listMyVoucher;

	// Đầu tiên là lấy ra các quyền = bản role
	// Nó chạy tới chỗ này nó lấy quyền ra kiểm tra có dùng được API không
	// Trong JWT Token Filter
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		// Phát hiện coi mình có cái Role gì
		// authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName()));

//		Mình cũng có thể Fake quyền để Test JWT Filter
//		authorityList.add(new SimpleGrantedAuthority("ADMIN"));
		authorityList.add(new SimpleGrantedAuthority("USER"));
		return authorityList;
	}

	// Spring Security nếu để nó bằng null thì nó sẽ hiểu
	// Trường duy nhất chính là Username
	@Override
	public String getUsername() {
		return email;// Cụ thể ở đây mình dùng Email để đăng nhập
	}

	// Account này có thời lượng vô thời hạn
	// True
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public int getUserId() {
		return userId;
	}

	public Role getRole() {
		return role;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getUrlProfileImage() {
		return urlProfileImage;
	}

	public boolean isActive() {
		return isActive;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public List<LearnerInterest> getListLearnerInterest() {
		return listLearnerInterest;
	}

	public List<LearnerSkill> getListLearnerSkill() {
		return listLearnerSkill;
	}

	public List<LearnerLevel> getListLearnerLevel() {
		return listLearnerLevel;
	}

	public List<LearnerGoal> getListLearnerGoal() {
		return listLearnerGoal;
	}

	public List<Voucher> getListVoucher() {
		return listVoucher;
	}

	public List<Payment> getListPayment() {
		return listPayment;
	}

	public List<RegisteredCourse> getListRegisteredCourse() {
		return listRegisteredCourse;
	}

	public List<Cart> getListCart() {
		return listCart;
	}

	public List<CourseProgress> getListCourseProgress() {
		return listCourseProgress;
	}

	public List<Course> getListCourse() {
		return listCourse;
	}

	public List<FavoriteCourse> getListFavoriteCourse() {
		return listFavoriteCourse;
	}

	public List<Comment> getListComment() {
		return listComment;
	}

	public List<Reply> getListReply() {
		return listReply;
	}

	public List<LessonComplete> getListLessonComplte() {
		return listLessonComplte;
	}

	public List<UserTestResult> getUserTestResult() {
		return UserTestResult;
	}

	public List<UserAnswerHistory> getListUserAnswerHistory() {
		return listUserAnswerHistory;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUrlProfileImage(String urlProfileImage) {
		this.urlProfileImage = urlProfileImage;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setListLearnerInterest(List<LearnerInterest> listLearnerInterest) {
		this.listLearnerInterest = listLearnerInterest;
	}

	public void setListLearnerSkill(List<LearnerSkill> listLearnerSkill) {
		this.listLearnerSkill = listLearnerSkill;
	}

	public void setListLearnerLevel(List<LearnerLevel> listLearnerLevel) {
		this.listLearnerLevel = listLearnerLevel;
	}

	public void setListLearnerGoal(List<LearnerGoal> listLearnerGoal) {
		this.listLearnerGoal = listLearnerGoal;
	}

	public void setListVoucher(List<Voucher> listVoucher) {
		this.listVoucher = listVoucher;
	}

	public void setListPayment(List<Payment> listPayment) {
		this.listPayment = listPayment;
	}

	public void setListRegisteredCourse(List<RegisteredCourse> listRegisteredCourse) {
		this.listRegisteredCourse = listRegisteredCourse;
	}

	public void setListCart(List<Cart> listCart) {
		this.listCart = listCart;
	}

	public void setListCourseProgress(List<CourseProgress> listCourseProgress) {
		this.listCourseProgress = listCourseProgress;
	}

	public void setListCourse(List<Course> listCourse) {
		this.listCourse = listCourse;
	}

	public void setListFavoriteCourse(List<FavoriteCourse> listFavoriteCourse) {
		this.listFavoriteCourse = listFavoriteCourse;
	}

	public void setListComment(List<Comment> listComment) {
		this.listComment = listComment;
	}

	public void setListReply(List<Reply> listReply) {
		this.listReply = listReply;
	}

	public void setListLessonComplte(List<LessonComplete> listLessonComplte) {
		this.listLessonComplte = listLessonComplte;
	}

	public void setUserTestResult(List<UserTestResult> userTestResult) {
		UserTestResult = userTestResult;
	}

	public void setListUserAnswerHistory(List<UserAnswerHistory> listUserAnswerHistory) {
		this.listUserAnswerHistory = listUserAnswerHistory;
	}

	public int getFacebookAccountId() {
		return facebookAccountId;
	}

	public int getGooogleAccountId() {
		return gooogleAccountId;
	}

	public void setFacebookAccountId(int facebookAccountId) {
		this.facebookAccountId = facebookAccountId;
	}

	public void setGooogleAccountId(int gooogleAccountId) {
		this.gooogleAccountId = gooogleAccountId;
	}

	public List<SocialAccount> getListSocialAccount() {
		return listSocialAccount;
	}

	public void setListSocialAccount(List<SocialAccount> listSocialAccount) {
		this.listSocialAccount = listSocialAccount;
	}

}
