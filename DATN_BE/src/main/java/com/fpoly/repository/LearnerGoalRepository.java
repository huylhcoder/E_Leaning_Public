package com.fpoly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.entity.LearnerGoal;

@Repository
public interface LearnerGoalRepository extends JpaRepository<LearnerGoal, Integer> {

}
