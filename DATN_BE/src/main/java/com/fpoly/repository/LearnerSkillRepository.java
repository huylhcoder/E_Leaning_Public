package com.fpoly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.entity.LearnerSkill;

@Repository
public interface LearnerSkillRepository extends JpaRepository<LearnerSkill, Integer> {

}
