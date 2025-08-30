package io.github.myeongseokkang.harusaegim.service;

import io.github.myeongseokkang.harusaegim.entity.AuthToken;
import io.github.myeongseokkang.harusaegim.entity.User;
import io.github.myeongseokkang.harusaegim.dto.LoginRequest;
import io.github.myeongseokkang.harusaegim.dto.SignupRequest;
import io.github.myeongseokkang.harusaegim.repository.AuthTokenRepository;
import io.github.myeongseokkang.harusaegim.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthTokenRepository tokenRepository;

    public AuthService(UserRepository userRepository, AuthTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) throw new IllegalStateException("email exists");
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPasswordHash(hash(req.getPassword()));
        u.setDisplayName(req.getDisplayName());
        userRepository.save(u);
    }

    @Transactional
    public String login(LoginRequest req) {
        Optional<User> user = userRepository.findByEmail(req.getEmail());
        if (user.isEmpty()) throw new IllegalArgumentException("invalid credentials");
        if (!user.get().getPasswordHash().equals(hash(req.getPassword()))) throw new IllegalArgumentException("invalid credentials");
        AuthToken t = new AuthToken();
        t.setToken(UUID.randomUUID().toString());
        t.setUser(user.get());
        t.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        tokenRepository.save(t);
        return t.getToken();
    }

    public Long authenticate(String token) {
        if (token == null) return null;
        Optional<AuthToken> t = tokenRepository.findByToken(token);
        if (t.isEmpty()) return null;
        if (t.get().getExpiresAt() != null && t.get().getExpiresAt().isBefore(Instant.now())) {
            tokenRepository.deleteByToken(token);
            return null;
        }
        return t.get().getUser().getId();
    }

    private String hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : dig) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
