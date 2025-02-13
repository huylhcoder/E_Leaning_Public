package com.fpoly.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.dto.AnswerDTO;
import com.fpoly.dto.CourseDetailDTO;
import com.fpoly.dto.LessonCourseDetailDTO;
import com.fpoly.dto.QuestionDTO;
import com.fpoly.dto.SectionCourseDetailDTO;
import com.fpoly.dto.TestDTO;
import com.fpoly.entity.Answer;
import com.fpoly.entity.Category;
import com.fpoly.entity.Course;
import com.fpoly.entity.HashTagOfCourse;
import com.fpoly.entity.Lesson;
import com.fpoly.entity.Question;
import com.fpoly.entity.RegisteredCourse;
import com.fpoly.entity.Section;
import com.fpoly.entity.Test;
import com.fpoly.entity.User;
import com.fpoly.entity.Voucher;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.service.CourseService;
import com.fpoly.service.LessonService;
import com.fpoly.service.RegisteredCourseService;
import com.fpoly.service.SectionService;
import com.fpoly.service.TestService;
import com.fpoly.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*") // cho phép bên ngoài truy xuất vào thoải mái k ngăn cản gì cả
@RestController
@RequestMapping("/api/course")
public class CourseController {
	@Autowired
	private CourseService courseService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private RegisteredCourseService registeredCourseService;
	@Autowired 
	 private TestService testService;
	@Autowired
	private  UserService UserService;
	@Autowired
    private JwtTokenUtils jwtTokenUtils;

//	@GetMapping
//	public List<Course> getAllCourse() {
//		return courseService.getAllCourse();
//	}
	// -- HBao code ----
	// LOAD DANH MỤC KHÓA HỌC VỚI STATUS = 1
	@GetMapping
    public ResponseEntity<?> getAllCourse(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();//Integer userId = 2;
		if (userId == null) {
            // Nếu userId là null, trả về tất cả các khóa học
        	return ResponseEntity.ok(courseService.getAllCourseWithSatus());
        } 
        if (userId != null){
            // Nếu userId không null, trả về các khóa học có sẵn cho người dùng
        	return ResponseEntity.ok(courseService.getUnregisteredCourse(userId));
        } else {
        	return ResponseEntity.badRequest().body(null);
        }
    }
	// - TÌM KIẾM KHÓA HỌC THEO GIÁ
	// + Miễn phí
	@GetMapping("/Free")
	public ResponseEntity<?> getFreeCourses(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseMienPhi());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseMienPhi(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// + Có phí
	@GetMapping("/UnFree")
	public List<Course> getUnFreeCourses() {
		return courseService.findCourseCoPhi();
	}
	
	// + Giá dưới 500.000
	@GetMapping("/SmallPrice")
	public ResponseEntity<?> getCoursesGiaBe(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseGiaBe());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseGiaBe(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// + Giá trên 500.000
	@GetMapping("/BigPrice")
	public ResponseEntity<?> getCoursesGiaLon(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseGiaLon());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseGiaLon(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// + Giá từ Min đến Max
	@GetMapping("/min/{minPrice}/max/{maxPrice}")
	public ResponseEntity<?> findCourseGiaMinMax(@RequestHeader("Authorization") String token, @PathVariable double minPrice, @PathVariable double maxPrice) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseGiaMinMax(minPrice, maxPrice));
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseGiaMinMax(userId, minPrice, maxPrice));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// - tìm khóa học theo tên
	@GetMapping("/search")
	public ResponseEntity<?> searchCourses(@RequestHeader("Authorization") String token, @RequestParam("name") String name) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.searchCoursesByName(name));
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.searchCoursesByNameLoginTrue(userId, name));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// - TÌM KIẾM KHÓA HỌC THEO SỐ SAO
	// + đánh giá 5 sao (đánh giá  = 5)
	@GetMapping("/Rate5Star")
	public ResponseEntity<?> findCourseAverageRating5(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating5());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating5(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// + đánh giá 4 sao trở lên (đánh giá từ 4 - 5)
	@GetMapping("/Rate4Star")
	public ResponseEntity<?> findCourseAverageRating4(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating4());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating4(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// + đánh giá 3 sao trở lên (đánh giá từ 3 - 5)
	@GetMapping("/Rate3Star")
	public ResponseEntity<?> findCourseAverageRating3(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating3());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating3(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// + đánh giá 2 sao trở lên (đánh giá từ 2 - 5)
	@GetMapping("/Rate2Star")
	public ResponseEntity<?> findCourseAverageRating2(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating2());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCourseAverageRating2(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// - DANH MỤC KHÓA HỌC
	@GetMapping("/getAllCategory")
	public List<Category> getAllCategory(){
		return courseService.getAllCategory();
	}
	
	// - Danh mục khóa học theo categoryId 
	@GetMapping("/findCategory/{categoryId}")
	public ResponseEntity<?> getCoursesByCategoryId(@RequestHeader("Authorization") String token, @PathVariable int categoryId) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.getCoursesByCategoryId(categoryId));
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.getCoursesByCategoryId(userId, categoryId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// - Danh sách khóa học theo Hashtag
	@GetMapping("/findCourseHashTag/{hashTagId}")
	public List<Course> getCoursesByHashTag(@PathVariable int hashTagId) {
        return courseService.getHashTagsByCourseId(hashTagId);
    }
	// - SẮP XẾP KHÓA HỌC THEO SỐ NGƯỜI THEO DÕI (TĂNG DẦN)
	@GetMapping("/Follow")
	public ResponseEntity<?> findCoursesByFollow(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCoursesByFollow());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCoursesByFollow(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// - SẮP XẾP KHÓA HỌC THEO GIÁ TIỀN TĂNG DẦN
	@GetMapping("/PriceASC")
	public ResponseEntity<?> findCoursesByPriceAsc(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCoursesByPriceAsc());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCoursesByPriceAsc(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// - SẮP XẾP KHÓA HỌC THEO GIÁ TIỀN GIẢM DẦN
	@GetMapping("/PriceDESC")
	public ResponseEntity<?> findCoursesByPriceDesc(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCoursesByPriceDesc());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCoursesByPriceDesc(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// - SẮP XẾP KHÓA HỌC THEO ĐÁNH GIÁ (GIẢM DẦN)
	@GetMapping("/TotalRateDESC")
	public ResponseEntity<?> findCoursesByAverageRatingDesc(@RequestHeader("Authorization") String token) {
		String FToken = token.replace("Bearer ", "").trim();
		System.out.println("Token: " + token);
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(FToken);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.err.println("Lỗi trích xuất Email từ token: " + e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}
		User user = UserService.getUserByEmailToan(email);
		Integer userId = user.getUserId();
		if (userId == null) {
			return ResponseEntity.ok(courseService.findCoursesByAverageRatingDesc());
		} 
		if (userId != null) {
			return ResponseEntity.ok(courseService.findCoursesByAverageRatingDesc(userId));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// Comment Vui vẻ 
	// LOAD DANH MỤC KHÓA HỌC VỚI STATUS = 1
	//	@GetMapping
	//	public List<Course> getAllCourse() {
	//		return courseService.getAllCourseWithSatus();
	//	}
	// - TÌM KIẾM KHÓA HỌC THEO GIÁ
	// + Miễn phí
	//	@GetMapping("/Free")
	//	public List<Course> getFreeCourses() {
	//		return courseService.findCourseMienPhi();
	//	}
	// + Giá dưới 500.000
	//	@GetMapping("/SmallPrice")
	//	public List<Course> getCoursesGiaBe() {
	//		return courseService.findCourseGiaBe();
	//	}
	// + Giá trên 500.000
	//	@GetMapping("/BigPrice")
	//	public List<Course> getCoursesGiaLon() {
	//		return courseService.findCourseGiaLon();
	//	}
	// + Giá từ Min đến Max
	//	@GetMapping("/min/{minPrice}/max/{maxPrice}")
	//	public List<Course> findCourseGiaMinMax(@PathVariable double minPrice, @PathVariable double maxPrice){
	//	    return courseService.findCourseGiaMinMax(minPrice, maxPrice);
	//	}
	// - TÌM KIẾM KHÓA HỌC THEO SỐ SAO
	// + đánh giá 5 sao (đánh giá  = 5)
	//		@GetMapping("/Rate5Star")
	//		public List<Course> findCourseAverageRating5(){
	//			return courseService.findCourseAverageRating5();
	//		}
	// + đánh giá 4 sao trở lên (đánh giá từ 4 - 5)
	//	@GetMapping("/Rate4Star")
	//	public List<Course> findCourseAverageRating4(){
	//		return courseService.findCourseAverageRating4();
	//	}
	// + đánh giá 3 sao trở lên (đánh giá từ 3 - 5)
	//	@GetMapping("/Rate3Star")
	//	public List<Course> findCourseAverageRating3(){
	//		return courseService.findCourseAverageRating3();
	//	}
	// + đánh giá 2 sao trở lên (đánh giá từ 2 - 5)
	//	@GetMapping("/Rate2Star")
	//	public List<Course> findCourseAverageRating2(){
	//		return courseService.findCourseAverageRating2();
	//	}
	// - Danh mục khóa học theo categoryId 
	//		@GetMapping("/findCategory/{categoryId}")
	//	    public List<Course> getCoursesByCategoryId(@PathVariable int categoryId) {
	//	        return courseService.getCoursesByCategoryId(categoryId);
	//	    }
	// - SẮP XẾP KHÓA HỌC THEO SỐ NGƯỜI THEO DÕI (TĂNG DẦN)
	//	@GetMapping("/Follow")
	//	public List<Course> findCoursesByFollow(){
	//		return courseService.findCoursesByFollow();
	//	}
	// - SẮP XẾP KHÓA HỌC THEO GIÁ TIỀN TĂNG DẦN
	//		@GetMapping("/PriceASC")
	//		public List<Course> findCoursesByPriceAsc(){
	//			return courseService.findCoursesByPriceAsc();
	//		}
	// - SẮP XẾP KHÓA HỌC THEO GIÁ TIỀN GIẢM DẦN
	//		@GetMapping("/PriceDESC")
	//		public List<Course> findCoursesByPriceDesc(){
	//			return courseService.findCoursesByPriceDesc();
	//		}
	//	@GetMapping("/TotalRateDESC")
	//	public List<Course> findCoursesByAverageRatingDesc(){
	//		return courseService.findCoursesByAverageRatingDesc();
	//	}
	
	@GetMapping("/checkRegisteredCourses")
	public ResponseEntity<Boolean> checkRegisteredCourse(@RequestParam int courseId, @RequestHeader("Authorization") String token) {
	    // Kiểm tra xem token có hợp lệ không
	    if (token == null || !token.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
	    }
	    String FToken = token.replace("Bearer ", "").trim();
	    System.out.println("Token: " + token);
	    String email;
	    try {
	        email = jwtTokenUtils.extractEmail(FToken);
	        System.out.println("Email: " + email);
	    } catch (Exception e) {
	        System.err.println("Error extracting email from token: " + e.getMessage());
	        return ResponseEntity.badRequest().body(false);
	    }
	    // Kiểm tra xem người dùng có tồn tại không
	    User user = UserService.getUserByEmailToan(email);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
	    }
	    Integer userId = user.getUserId();	    
	    // Kiểm tra xem người dùng đã đăng ký khóa học hay chưa
	    boolean isRegistered = courseService.checkIfRegistered(courseId, userId);
	    return ResponseEntity.ok(isRegistered);
	}
	
	// -- Hết phần code của HBao -----
	
//	@GetMapping("/course-detail/{id}")
//    public Course getOneHao(@PathVariable("id") int id) {
//        return courseService.getCourseID(id).get();
//    }
	@GetMapping("/course-detail/{id}")
	public ResponseEntity<CourseDetailDTO> hienThiChiTietKhoaHoc(@PathVariable("id") int CourseId) {
		Course CourseEntity = courseService.hienThiKhoaHocTheoIdHao(CourseId);
		List<Section> listSectionEntity = sectionService.hienThiSectionTheoKhoaHocHao(CourseEntity);
		
		List<SectionCourseDetailDTO> listSectionCourseDetailDTO = new ArrayList<>();
		for (Section sectionEntity : listSectionEntity) {
			
			//
			List<Lesson> listLessonEntity = lessonService.hienThiLessonTheoSection(sectionEntity);
			List<LessonCourseDetailDTO> listLessonDTO = new ArrayList<>();
			for (Lesson lessonEntity : listLessonEntity) {
				LessonCourseDetailDTO lessonDTO = new LessonCourseDetailDTO();
				lessonDTO.setLessonId(lessonEntity.getLessonId());
				lessonDTO.setLessonName(lessonEntity.getName());
				lessonDTO.setDescription(lessonEntity.getDescription());
				lessonDTO.setPathVideo(lessonEntity.getPathVideo());
				listLessonDTO.add(lessonDTO);
				
			}
				
				//
				List<Test> listTestEntity = testService.hienThiTestTheoSection(sectionEntity);
				List<TestDTO> listTestDTO = new ArrayList<>();
				for (Test testEntity : listTestEntity) {
					TestDTO testDTO = new TestDTO();
					testDTO.setTestID(testEntity.getTestId());
					testDTO.setTitle(testEntity.getTitle());
					listTestDTO.add(testDTO);

				
				
			}
			
			SectionCourseDetailDTO sectionDTO = new SectionCourseDetailDTO();
			sectionDTO.setSectionId(sectionEntity.getSectionId());
			sectionDTO.setSectionName(sectionEntity.getName());
			sectionDTO.setListLesson(listLessonDTO);
			sectionDTO.setListTest(listTestDTO);
			listSectionCourseDetailDTO.add(sectionDTO);
			
		}
		CourseDetailDTO coursedetailDTO = new CourseDetailDTO();
		coursedetailDTO.setCourseId(CourseEntity.getCourseId());
		coursedetailDTO.setAvatar(CourseEntity.getAvatar());
		coursedetailDTO.setCategoryName(CourseEntity.getCategory().getName());
		coursedetailDTO.setName(CourseEntity.getName());
		coursedetailDTO.setDescription(CourseEntity.getDescription());
		coursedetailDTO.setName(CourseEntity.getName());
		coursedetailDTO.setPrice(CourseEntity.getPrice());
		coursedetailDTO.setAverageRating(CourseEntity.getAverageRating());
		coursedetailDTO.setTotalRate(CourseEntity.getTotalRate());
		coursedetailDTO.setListSectionCourseDetailDTO(listSectionCourseDetailDTO);
		return ResponseEntity.ok(coursedetailDTO);
	}
	
	@GetMapping("/course-detail1/{id}")
    public ResponseEntity<Course> getCourseDetail(@PathVariable int id) {
        Course course = courseService.hienThiKhoaHocTheoIdHao(id);
        return ResponseEntity.ok(course);
    }
	

    // API tìm kiếm các khóa học theo danh mục
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Course>> getCoursesByCategory(@PathVariable String categoryName) {
        List<Course> courses = courseService.findCoursesByCategory(categoryName);
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/regiteredCourse/{token}")
    public ResponseEntity<List<RegisteredCourse>> GetRegisteredCourseByUserHao(@PathVariable("token") String token) {
    	System.out.println("Token received: " + token);
    	
		String email;
        
            email = jwtTokenUtils.extractEmail(token);
            System.out.println("Extracted email: " + jwtTokenUtils.extractEmail(token));
            User user = UserService.getUserByEmailToan(email);
            


//Copy khúc này
    	List<RegisteredCourse> registeredCourse =registeredCourseService.findRegisteredCourseByUserHao(user.getUserId());
        return ResponseEntity.ok(registeredCourse);
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<List<Course>> getTopRatedCourses() {
        List<Course> courses = courseService.getTopRatedCourses();
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/follow")
    public ResponseEntity<List<Course>> getTopFollowCourses() {
        List<Course> courses = courseService.getTopFollowCourses();
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/name")
    public ResponseEntity<Long> getCourses() {
        Long courses = courseService.getNameCourses();
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/users")
    public ResponseEntity<Long> getNameUser() {
        Long courses = courseService.getUserCourses();
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/cate")
    public ResponseEntity<Long> getCateCourses() {
        Long courses = courseService.getCateCourses();
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/star")
    public ResponseEntity<Double> getStarCourses() {
    	Double courses = courseService.getStarCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/rdVoucher")
    public ResponseEntity<List<Voucher>> getRDVoucher() {
        List<Voucher> voucher = courseService.getRDVoucher();
        return ResponseEntity.ok(voucher);
    }
}
