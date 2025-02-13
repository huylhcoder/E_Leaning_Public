package com.fpoly.dto;

import java.util.Date;
import java.util.List;

import com.fpoly.entity.Category;
import com.fpoly.entity.CourseLevel;
import com.fpoly.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

public class CourseProgressRequest {
	 private Integer currentLessionId;
	    private Integer totalLession;
	    private Integer totalQuiz;
	    private Integer totalLessionComplete;
	    private Integer totalTestComplete;
	    private Float progressPercentage;
	    private Integer progressStatus;

	    // Getters và setters
	    public Integer getCurrentLessionId() {
	        return currentLessionId;
	    }

	    public void setCurrentLessionId(Integer currentLessionId) {
	        this.currentLessionId = currentLessionId;
	    }

	    public Integer getTotalLession() {
	        return totalLession;
	    }

	    public void setTotalLession(Integer totalLession) {
	        this.totalLession = totalLession;
	    }

	    public Integer getTotalQuiz() {
	        return totalQuiz;
	    }

	    public void setTotalQuiz(Integer totalQuiz) {
	        this.totalQuiz = totalQuiz;
	    }

	    public Integer getTotalLessionComplete() {
	        return totalLessionComplete;
	    }

	    public void setTotalLessionComplete(Integer totalLessionComplete) {
	        this.totalLessionComplete = totalLessionComplete;
	    }

	    public Integer getTotalTestComplete() {
	        return totalTestComplete;
	    }

	    public void setTotalTestComplete(Integer totalTestComplete) {
	        this.totalTestComplete = totalTestComplete;
	    }

	    public Float getProgressPercentage() {
	        return progressPercentage;
	    }

	    public void setProgressPercentage(Float progressPercentage) {
	        this.progressPercentage = progressPercentage;
	    }

	    public Integer getProgressStatus() {
	        return progressStatus;
	    }

	    public void setProgressStatus(Integer progressStatus) {
	        this.progressStatus = progressStatus;
	    }
	
}
