package com.fpoly.service;

import java.util.List;
import java.util.Optional;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Course;
import com.fpoly.entity.CourseProgress;
import com.fpoly.entity.User;
import com.fpoly.entity.Voucher;
import com.fpoly.repository.AnswerRepository;
import com.fpoly.repository.CourseProgressRepository;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpoly.repository.CourseProgressRepository;

@Service
public class CourseProgressService {
	@Autowired
	CourseProgressRepository courseProgressRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CourseRepository courseReponsitory;

	public List<CourseProgress> FillCourseKhoa(int userId){
		return courseProgressRepository.FillCourseKhoa(userId);
	}

	public User updateStatusKhoa(User user) {
		return userRepository.save(user);
	}

	public CourseProgress findCourseProgressByIdKhoa(int userId) {
		return courseProgressRepository.findByCourseProgressId(userId);
	}

	// CODE HBao
	public List<CourseProgress> FillTotalCourseComplete() {
		return courseProgressRepository.FillTotalCourseCompleteCuaBao();
	}

	public CourseProgress findByUserAndCourse(User user, Course course) {
		return courseProgressRepository.findByUserAndCourse(user, course);
	}

	public CourseProgress save(CourseProgress courseProgress) {
		return courseProgressRepository.save(courseProgress);
	}

	// Cập nhật tiến độ khi kiểm tra
	public CourseProgress updateCourseProgressForTakeTheTest(User user, Course course) {
		CourseProgress courseProgressUpDate = new CourseProgress();
		// Tìm kiếm tiến độ của bài học
		courseProgressUpDate = courseProgressRepository.findByUserAndCourse(user, course);
		// Nếu người dùng đã hoàn thành bài kiểm tra thì cộng lên một
		courseProgressUpDate.setTotalTestComplete(courseProgressUpDate.getTotalTestComplete() + 1);
		// Tính lại phần trăm hoàn thành
		courseProgressUpDate.setProgressPercentage(calculateProgressPercentage(courseProgressUpDate));
		return courseProgressRepository.save(courseProgressUpDate);
	}

	public float calculateProgressPercentage(CourseProgress courseProgress) {
		// Giả sử bạn có tổng số bài học và bài kiểm tra
		int totalLessons = courseProgress.getTotalLession();
		int totalQuizzes = courseProgress.getTotalQuiz();
		int totalCompleted = courseProgress.getTotalLessionComplete() + courseProgress.getTotalTestComplete();

		if (totalLessons + totalQuizzes == 0) {
			return 0;
		}

		return (float) totalCompleted / (totalLessons + totalQuizzes) * 100;
	}

    public CourseProgress getCourseProgress(int courseId, int userId) {
        return courseProgressRepository.findByCourse_CourseIdAndUser_UserId(courseId, userId)
                                          .orElse(null);  // Trả về null nếu không tìm thấy tiến độ
    }

    // Cập nhật tiến độ khóa học và tính toán lại progressPercentage
    public CourseProgress updateProgressPercentage(CourseProgress courseProgress) {
        // Tính lại tiến độ khóa học dựa trên số lượng bài học và bài kiểm tra hoàn thành
        float progress = 0;
        // Tránh chia cho 0 bằng cách kiểm tra nếu tổng bài học và quiz lớn hơn 0
        if ((courseProgress.getTotalLession() + courseProgress.getTotalQuiz()) > 0) {
            progress = (float)(courseProgress.getTotalLessionComplete() + courseProgress.getTotalTestComplete()) 
                       / (courseProgress.getTotalLession() + courseProgress.getTotalQuiz()) * 100;
        }

        // Làm tròn tiến độ về số nguyên (1 con số phía trước dấu phẩy)
//        progress = Math.round(progress);

        courseProgress.setProgressPercentage(progress);

        // Lưu lại tiến độ khóa học đã cập nhật vào cơ sở dữ liệu
        return courseProgressRepository.save(courseProgress);
    }

    // Tạo hoặc cập nhật tiến độ khóa học (cho trường hợp không tồn tại tiến độ)
    public CourseProgress createOrUpdateCourseProgress(CourseProgress courseProgress) {
        // Kiểm tra nếu đã có tiến độ của khóa học này cho người dùng
        CourseProgress existingProgress = getCourseProgress(courseProgress.getCourse().getCourseId(), courseProgress.getUser().getUserId());

        if (existingProgress != null) {
            // Cập nhật tiến độ hiện tại
            return updateProgressPercentage(courseProgress);
        } else {
            // Nếu không có tiến độ, lưu mới
            // Tính toán lại tiến độ trước khi lưu
            return courseProgressRepository.save(courseProgress);
        }
    }
  

   


    
}
