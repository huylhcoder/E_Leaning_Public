package com.fpoly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.entity.LearnerInterest;

@Repository
public interface LearnerInterestRepository extends JpaRepository<LearnerInterest, Integer> {

}
