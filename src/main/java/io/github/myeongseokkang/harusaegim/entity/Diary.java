package io.github.myeongseokkang.harusaegim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Entity
public class Diary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private Long userId;
    @Setter
    private LocalDate date;
    @Setter
    @Column(length = 8000)
    private String content;
    @Setter
    private Integer emotionScore;
    private Instant createdAt;
    private Instant updatedAt;
    @PrePersist
    public void prePersist() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }

}
