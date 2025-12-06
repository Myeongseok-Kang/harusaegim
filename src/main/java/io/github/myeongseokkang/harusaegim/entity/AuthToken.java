package io.github.myeongseokkang.harusaegim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Entity
public class AuthToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false, unique = true)
    private String token;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant createdAt;
    @Setter
    private Instant expiresAt;
    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }
}
