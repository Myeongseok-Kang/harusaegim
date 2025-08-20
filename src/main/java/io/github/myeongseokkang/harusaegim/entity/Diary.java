package io.github.myeongseokkang.harusaegim.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
public class Diary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDate date;
    @Column(length = 8000)
    private String content;
    private Integer emotionScore;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getEmotionScore() { return emotionScore; }
    public void setEmotionScore(Integer emotionScore) { this.emotionScore = emotionScore; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
