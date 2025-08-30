package io.github.myeongseokkang.harusaegim.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class AuthToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant createdAt;
    private Instant expiresAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
