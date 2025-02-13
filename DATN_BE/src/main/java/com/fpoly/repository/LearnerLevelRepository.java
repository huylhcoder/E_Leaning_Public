package com.fpoly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.entity.LearnerLevel;

@Repository
public interface LearnerLevelRepository extends JpaRepository<LearnerLevel, Integer> {

}
