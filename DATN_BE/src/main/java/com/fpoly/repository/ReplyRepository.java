package com.fpoly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.entity.Answer;
import com.fpoly.entity.Comment;
import com.fpoly.entity.CourseProgress;
import com.fpoly.entity.Reply;
import com.fpoly.entity.User;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
	@Query("SELECT r FROM Reply r WHERE r.comment.id = :commentId")
	List<Reply> findByCommentId(@Param("commentId") int commentId);

	Reply findByComment(Comment comment);
}
