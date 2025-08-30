package io.github.myeongseokkang.harusaegim.repository;

import io.github.myeongseokkang.harusaegim.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);
    void deleteByToken(String token);
}
