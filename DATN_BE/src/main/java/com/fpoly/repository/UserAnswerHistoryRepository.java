package com.fpoly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fpoly.entity.Question;
import com.fpoly.entity.User;
import com.fpoly.entity.UserAnswerHistory;
import com.fpoly.entity.Test;


@Repository
public interface UserAnswerHistoryRepository extends JpaRepository<UserAnswerHistory, Integer> {
	@Query("SELECT u FROM UserAnswerHistory u WHERE u.user.id = :userId AND u.question.id = :questionId")
	Optional<UserAnswerHistory> findByUserAndQuestion(@Param("userId") int userId, @Param("questionId") int questionId);

	UserAnswerHistory findByUserAndQuestion(User user, Question question);

	List<UserAnswerHistory> findByUserAndTest(User user, Test test);

	@Modifying
    @Transactional
    @Query("DELETE FROM UserAnswerHistory u WHERE u.user.id = ?1 AND u.test.id = ?2")
    void deleteByUserAndTestId(int userId, int testId);
}