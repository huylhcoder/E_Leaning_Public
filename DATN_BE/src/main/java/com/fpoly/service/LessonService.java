package com.fpoly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpoly.entity.Course;
import com.fpoly.entity.Lesson;
import com.fpoly.entity.Section;
import com.fpoly.entity.Test;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.LessonRepository;
import com.fpoly.repository.SectionRepository;

@Service
public class LessonService {
	@Autowired
	LessonRepository lessonRepository;
	@Autowired
	SectionRepository sectionRepository;
	@Autowired
	CourseRepository courseRepository;

	public List<Lesson> hienThiLessonTheoSection(Section sectionEntity) {
		List<Lesson> listLesson = lessonRepository.findBySection(sectionEntity);
		return listLesson;
	}

	// Lấy danh sách bài học theo phần
	public List<Lesson> getLessonByLesson(Section section) {
		return lessonRepository.findBySection(section);
	}

	// Tìm bài học theo id
	public Optional<Lesson> getLessonById_Huy(int lesson) {
		return lessonRepository.findById(lesson);
	}

	// Thêm câu hỏi mới
	public Lesson addLesson(Lesson lesson) {
		return lessonRepository.save(lesson);
	}

	// Thêm câu hỏi mới
	public Lesson updateLesson(Lesson lesson) {
		return lessonRepository.save(lesson);
	}

	// Xóa bài học
	public void removeLesson(Lesson lesson) {
		lessonRepository.delete(lesson);
	}

	// Cập nhật thời lượng bài học, phần và khóa học
	@Transactional
	public void updateLessonDuration(int lessonId, float newDuration) {
		// Cập nhật thời gian bài học
		Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found"));

		// Lưu thời gian mới
		lesson.setLessionDuration(newDuration);
		lessonRepository.save(lesson);

		// Cập nhật thời gian cho phần
		Section section = lesson.getSection();
		updateSectionDuration(section);

		// Cập nhật thời gian cho khóa học
		Course course = section.getCourse();
		updateCourseDuration(course);
	}

	private void updateSectionDuration(Section section) {
		List<Lesson> lessons = section.getListLesson();
		float totalDuration = lessons.stream().map(Lesson::getLessionDuration).reduce(0f, Float::sum);

		section.setSectionDuration(totalDuration);
		sectionRepository.save(section);
	}

	private void updateCourseDuration(Course course) {
		List<Section> sections = course.getListSection();
		float totalDuration = sections.stream().map(Section::getSectionDuration).reduce(0f, Float::sum);

		course.setCourseDuration(totalDuration);
		courseRepository.save(course);
	}
}
