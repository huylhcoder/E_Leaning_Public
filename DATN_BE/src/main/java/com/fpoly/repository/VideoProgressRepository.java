package com.fpoly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.entity.VideoProgress;

public interface VideoProgressRepository extends JpaRepository<VideoProgress, Integer> {
	Optional<VideoProgress> findByUserIdAndLessonId(int userId, int lessonId);
}
