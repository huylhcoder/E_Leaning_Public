package com.fpoly.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpoly.entity.Category;
import com.fpoly.entity.Course;
import com.fpoly.repository.CategoryRepository;
import com.fpoly.dto.CourseDetailManagerDTO;
import com.fpoly.entity.Category;
import com.fpoly.entity.Course;
import com.fpoly.entity.CourseLevel;
import com.fpoly.entity.HashTagOfCourse;
import com.fpoly.entity.Lesson;
import com.fpoly.entity.RegisteredCourse;
import com.fpoly.entity.Section;
import com.fpoly.entity.Test;
import com.fpoly.entity.Voucher;
import com.fpoly.repository.CategoryRepository;
import com.fpoly.repository.CourseLevelRepository;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.HashtagOfCourseRepository;
import com.fpoly.repository.LessonRepository;
import com.fpoly.repository.RegisteredCourseRepository;
import com.fpoly.repository.SectionRepository;
import com.fpoly.repository.TestRepository;
import com.fpoly.repository.VoucherRepository;

@Service
public class CourseService {
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	CourseLevelRepository levelRepo;
	@Autowired
	HashtagOfCourseRepository hashtagOfCourseRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private RegisteredCourseRepository registeredCourseRepository;
    
	public Course timKhoaHocTheoMaKhoaHocToan(int courseId) {
		return courseRepository.findByCourseId(courseId);
	}

	public List<Course> getAllCourse() {
		return courseRepository.findAll();
	}

	public Optional<Course> timKhoaHocTheoMaKhoaHocHuy(int courseId) {
		return courseRepository.findById(courseId);
	}

	public Optional<Course> timKhoaHocTheoMaKhoaHocTam(int courseId) {
		return courseRepository.findById(courseId);
	}

	// HBảo code
	// LẤY DANH SÁCH KHÓA HỌC KHI NGƯỜI DÙNG ĐÃ ĐĂNG NHẬP VÀ LOẠI BỎ CÁC KHÓA HỌC
	// - 1. LẤY DANH SÁCH KHÓA HỌC CÓ TRẠNG THÁI Status = 1
	// + LẤY DANH SÁCH KHÓA TẤT CẢ KHÓA HỌC KHI NGƯỜI DÙNG "CHƯA ĐĂNG NHẬP"
	public List<Course> getAllCourseWithSatus() {
		return courseRepository.getAllCourseWithSatus();
	}

	// + LẤY DANH SÁCH KHÓA HỌC KHI NGƯỜI DÙNG "ĐÃ ĐĂNG NHẬP" VÀ LOẠI BỎ CÁC KHÓA
	public List<Course> getUnregisteredCourse(int userId) {
		return courseRepository.findUnregisteredCourse(userId);
	}

	// - 2. TÌM KIẾM KHÓA HỌC THEO TÊN NGƯỜI DÙNG NHẬP CÓ TRẠNG THÁI Status = 1
	// + TÌM KIẾM KHÓA HỌC THEO TÊN NGƯỜI DÙNG NHẬP KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> searchCoursesByName(String name) {
		return courseRepository.findCourseTheoTen(name);
	}

	// + TÌM KIẾM KHÓA HỌC THEO TÊN NGƯỜI DÙNG NHẬP KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> searchCoursesByNameLoginTrue(int userId, String name) {
		return courseRepository.findCourseTheoTenLoginTrue(userId, name);
	}

	// - 3. TÌM KIẾM KHÓA HỌC MIỄN PHÍ VỚI TRẠNG THÁI Status = 1
	// + TÌM KIẾM KHÓA HỌC MIỄN PHÍ KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseMienPhi() {
		return courseRepository.findCourseMienPhi();
	}

	// + TÌM KIẾM KHÓA HỌC MIỄN PHÍ KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseMienPhi(int userId) {
		return courseRepository.findCourseMienPhi(userId);
	}

	// - 4. TÌM KIẾM KHÓA HỌC CÓ PHÍ VỚI TRẠNG THÁI Status = 1
	public List<Course> findCourseCoPhi() {
		return courseRepository.findCourseCoPhi();
	}

	// - 5. TÌM KIẾM KHÓA HỌC CÓ GIÁ > 500 VỚI TRẠNG THÁI Status = 1
	// + TÌM KIẾM KHÓA HỌC CÓ GIÁ > 500 KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseGiaLon() {
		return courseRepository.findCourseGiaLon();
	}

	// + TÌM KIẾM KHÓA HỌC CÓ GIÁ > 500 KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseGiaLon(int userId) {
		return courseRepository.findCourseGiaLon(userId);
	}

	// - 6. TÌM KIẾM KHÓA HỌC CÓ GIÁ < 500 VỚI TRẠNG THÁI Status = 1
	// + TÌM KIẾM KHÓA HỌC CÓ GIÁ < 500 KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseGiaBe() {
		return courseRepository.findCourseGiaBe();
	}

	// + TÌM KIẾM KHÓA HỌC CÓ GIÁ < 500 KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseGiaBe(int userId) {
		return courseRepository.findCourseGiaBe(userId);
	}

	// - 7. TÌM KIẾM KHÓA HỌC THEO KHOẢNG GIÁ TỪ MAX ĐẾN MIN
	// + TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseGiaMinMax(double minPrice, double maxPrice) {
		return courseRepository.findCourseGiaMinMax(minPrice, maxPrice);
	}

	// + TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseGiaMinMax(int userId, double minPrice, double maxPrice) {
		return courseRepository.findCourseGiaMinMax(userId, minPrice, maxPrice);
	}

	// - 8. TÌM KIẾM KHÓA HỌC THEO ĐÁNH GIÁ VỚI TRẠNG THÁI Status = 1
	// - 8.1 TÌM KHÓA HỌC VỚI ĐÁNH GIÁ BẰNG 5
	// + TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating5() {
		return courseRepository.findCourseAverageRating5();
	}

	// + TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating5(int userId) {
		return courseRepository.findCourseAverageRating5(userId);
	}

	// - 8.2 TÌM KHÓA HỌC VỚI ĐÁNH GIÁ TỪ 4 ĐẾN 5
	// + TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating4() {
		return courseRepository.findCourseAverageRating4();
	}

	// + TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating4(int userId) {
		return courseRepository.findCourseAverageRating4(userId);
	}

	// - 8.3 TÌM KHÓA HỌC VỚI ĐÁNH GIÁ TỪ 3 ĐẾN 5
	// + TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating3() {
		return courseRepository.findCourseAverageRating3();
	}

	// + TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating3(int userId) {
		return courseRepository.findCourseAverageRating3(userId);
	}

	// - 8.4 TÌM KHÓA HỌC VỚI ĐÁNH GIÁ TỪ 2 ĐẾN 5
	// + TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating2() {
		return courseRepository.findCourseAverageRating2();
	}

	// + TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCourseAverageRating2(int userId) {
		return courseRepository.findCourseAverageRating2(userId);
	}

	// - 9. TÌM DANH MỤC KHÓA HỌC THEO CATEGORY_ID VÀ TRẠNG THÁI status = 1
	// - 9.1 LOAD DANH MỤC
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}

	// - 9.2 TÌM KHÓA HỌC THEO Category_Id
	// + TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> getCoursesByCategoryId(int categoryId) {
		return courseRepository.findCourseByCategoryId(categoryId);
	}

	// + TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> getCoursesByCategoryId(int userId, int categoryId) {
		return courseRepository.findCourseByCategoryId(userId, categoryId);
	}

	// - 10. SẮP XẾP KHÓA HỌC LƯỢT THEO ĐĂNG KÝ (THEO DÕI) VỚI TRẠNG THÁI Status = 1
	// TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCoursesByFollow() {
		return courseRepository.findCoursesByFollow();
	}

	// TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCoursesByFollow(int userId) {
		return courseRepository.findCoursesByFollow(userId);
	}

	// - 11. SẮP XẾP KHÓA HỌC THEO ĐÁNH GIÁ VỚI TRẠNG THÁI Status = 1
	// TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCoursesByAverageRatingDesc() {
		return courseRepository.findCoursesByAverageRatingDesc();
	}

	// TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCoursesByAverageRatingDesc(int userId) {
		return courseRepository.findCoursesByAverageRatingDesc(userId);
	}

	// - 12. SẮP XẾP KHÓA HỌC THEO GIÁ TĂNG DẦN VỚI TRẠNG THÁI Status = 1
	// TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCoursesByPriceAsc() {
		return courseRepository.findCoursesByPriceAsc();
	}
	
	// TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCoursesByPriceAsc(int userId) {
		return courseRepository.findCoursesByPriceAsc(userId);
	}
	
	// - 13. SẮP XẾP KHÓA HỌC THEO GIÁ GIẢM DẦN VỚI TRẠNG THÁI Status = 1
	// TÌM KIẾM KHÓA HỌC KHI "CHƯA ĐĂNG NHẬP"
	public List<Course> findCoursesByPriceDesc() {
		return courseRepository.findCoursesByPriceDesc();
	}
	
	// TÌM KIẾM KHÓA HỌC KHI "ĐÃ ĐĂNG NHẬP"
	public List<Course> findCoursesByPriceDesc(int userId) {
		return courseRepository.findCoursesByPriceDesc(userId);
	}
	
	// - 14. TÌM KIẾM KHÓA HỌC TRONG KHÓA HỌC ĐÃ ĐĂNG KÝ 
	public List<Course> findCoursesOnRegisteredCourse(int courseId, int userId){
		return courseRepository.findCoursesOnRegisteredCourse(courseId, userId);
	}
	
	// TEST ĐÃ MUA
	public boolean checkIfRegistered(int courseId, int userId) {
        return registeredCourseRepository.existsByCourseIdAndUserId(courseId, userId);
    }
	
	//------------------------- HẾT CODE CỦA HBAO ------------------------ 
	
	public List<Course> getHashTagsByCourseId(int hashTagId) {
		return courseRepository.findCoursesByHashTag(hashTagId);
	}
	
	public Course hienThiKhoaHocTheoIdHao(int id) {
		return courseRepository.findByCourseId(id);
	}

	public List<Course> findCoursesByCategory(String categoryName) {
		return courseRepository.findByCategoryNameIgnoreCase(categoryName);
	}

	public List<Course> getCoursesByCategory(Category category) {
		return courseRepository.findByCategory(category);
	}

	public List<Course> hienThiKhoaHocTheoTrangThaiKhoaHoc_Huy(List<Integer> trangThai) {
		return courseRepository.findByStatusIn(trangThai);
	}

	// Tìm kiếm khóa học mới được tạo
	public Course getLatestCourse() {
		return courseRepository.findCourseWithMaxId();
	}

	public Course themKhoaHocMoi_Huy(Course course) {
		return courseRepository.save(course);
	}

	public CourseDetailManagerDTO luuThongTinKhoaHoc(CourseDetailManagerDTO courseDTO) {
		Course courseEntity = new Course();
		courseEntity.setCourseId(courseDTO.getCourseId());
		courseEntity.setName(courseDTO.getName());
		courseEntity.setStatus(courseDTO.getStatus());
		courseEntity.setDescription(courseDTO.getDescription());
		courseEntity.setAvatar(courseDTO.getAvatar());
		courseEntity.setPrice(courseDTO.getPrice());
		courseEntity.setTopic(courseDTO.getTopic());
		Category category = categoryRepo.findByCategoryId(courseDTO.getCategoryId());
		courseEntity.setCategory(category);
		CourseLevel level = levelRepo.findById(courseDTO.getLevelId()).orElse(null);
		courseEntity.setCourseLevel(level);
		courseEntity.setUpdateAt(new Date());
		courseRepository.save(courseEntity);
		// Trả về DTo cho để hiển thị
		return courseDTO;
	}

	public List<Course> getTopRatedCourses() {
		return courseRepository.findTopRatedCourses();
	}

	public List<Course> getTopFollowCourses() {
		return courseRepository.findTop4ByStatusOrderByFollowDesc(1);
	}

	public Long getNameCourses() {
		return courseRepository.countCourseNames();
	}

	public Long getCateCourses() {
		return courseRepository.countCateNames();
	}

	public Double getStarCourses() {
		return courseRepository.countStarAVG();
	}

	public Long getUserCourses() {
		return courseRepository.countUserNames();
	}

	@Autowired
	VoucherRepository voucherRepository;

	public List<Voucher> getRDVoucher() {
		return voucherRepository.findRandomVouchers();
	}
	
	//Xóa khóa học nháp
	@Transactional
    public void removeDraftCourse(int courseId) {
        // Tìm khóa học
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));

        // Xóa tất cả các phần liên quan đến khóa học
        for (Section section : course.getListSection()) {
            // Xóa tất cả các bài học trong phần
            for (Lesson lesson : section.getListLesson()) {
                lessonRepository.delete(lesson);
            }
            // Xóa tất cả các bài kiểm tra trong phần
            for (Test test : section.getListTest()) {
                testRepository.delete(test);
            }
            // Xóa phần
            sectionRepository.delete(section);
        }

        // Cuối cùng, xóa khóa học
        courseRepository.delete(course);
    }
}
