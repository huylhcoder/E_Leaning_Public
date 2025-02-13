package com.fpoly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.entity.Course;
import com.fpoly.entity.Lesson;
import com.fpoly.entity.Section;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

	List<Lesson> findBySection(Section sectionEntity);
	
}
