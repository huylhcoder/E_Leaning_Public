package com.fpoly.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "video_progress")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class VideoProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    @Column(name = "video_progress_id")
    private int videoProgressId;

    @Column(name = "users_id", nullable = false)
    private int userId;

    @Column(name = "registered_course_id", nullable = false)
    private int registeredCourseId;

    @Column(name = "lesson_id", nullable = false)
    private int lessonId;

    @Column(name = "path_video", length = 250, nullable = false)
    private String pathVideo;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime update_at;

    @Column(name = "video_progress", nullable = false)
    private int videoProgress;


    // Constructors
    public VideoProgress() {
    }

    public VideoProgress(int userId, int registeredCourseId, int lessonId, String pathVideo, 
                         LocalDateTime update_at, int videoProgress) {
        this.userId = userId;
        this.registeredCourseId = registeredCourseId;
        this.lessonId = lessonId;
        this.pathVideo = pathVideo;
        this.update_at = update_at;
        this.videoProgress = videoProgress;
        
    }

    // Getters and Setters
    public int getVideoProgressId() {
        return videoProgressId;
    }

    public void setVideoProgressId(int videoProgressId) {
        this.videoProgressId = videoProgressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRegisteredCourseId() {
        return registeredCourseId;
    }

    public void setRegisteredCourseId(int registeredCourseId) {
        this.registeredCourseId = registeredCourseId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getPathVideo() {
        return pathVideo;
    }

    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
    }

    public LocalDateTime getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(LocalDateTime update_at) {
        this.update_at = update_at;
    }

    public int getVideoProgress() {
        return videoProgress;
    }

    public void setVideoProgress(int videoProgress) {
        this.videoProgress = videoProgress;
    }

    @Override
    public String toString() {
        return "VideoProgress{" +
                "videoProgressId=" + videoProgressId +
                ", userId=" + userId +
                ", registeredCourseId=" + registeredCourseId +
                ", lessonId=" + lessonId +
                ", pathVideo='" + pathVideo + '\'' +
                ", update_at=" + update_at +
                ", videoProgress=" + videoProgress +                
                '}';
    }
}
