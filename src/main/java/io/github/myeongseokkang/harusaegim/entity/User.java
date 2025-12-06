package io.github.myeongseokkang.harusaegim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false, unique = true)
    private String email;
    @Setter
    @Column(nullable = false)
    private String passwordHash;
    @Setter
    private String displayName;
    private Instant createdAt;
    private Instant updatedAt;
    @PrePersist
    public void prePersist() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}
