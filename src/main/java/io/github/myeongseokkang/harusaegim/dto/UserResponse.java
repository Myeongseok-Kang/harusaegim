package io.github.myeongseokkang.harusaegim.dto;

import io.github.myeongseokkang.harusaegim.entity.User;

import java.time.Instant;

public record UserResponse(
        Long id,
        String email,
        String displayName,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
