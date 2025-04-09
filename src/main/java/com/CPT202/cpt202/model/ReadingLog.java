package com.CPT202.cpt202.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_logs")
public class ReadingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer readingLogId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user; // 关联到 User 表

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(nullable = false)
    private LocalDate readingDate;

    @Column(nullable = false)
    private Integer readingTime; // 阅读时长，单位：分钟

    @Column(columnDefinition = "TEXT")
    private String personalNotes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public ReadingLog() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getter 和 Setter 方法

    public Integer getReadingLogId() {
        return readingLogId;
    }

    public void setReadingLogId(Integer readingLogId) {
        this.readingLogId = readingLogId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(LocalDate readingDate) {
        this.readingDate = readingDate;
    }

    public Integer getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(Integer readingTime) {
        this.readingTime = readingTime;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String personalNotes) {
        this.personalNotes = personalNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}