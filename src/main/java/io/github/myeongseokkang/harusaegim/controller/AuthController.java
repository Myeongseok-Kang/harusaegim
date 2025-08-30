package io.github.myeongseokkang.harusaegim.controller;

import io.github.myeongseokkang.harusaegim.dto.LoginRequest;
import io.github.myeongseokkang.harusaegim.dto.LoginResponse;
import io.github.myeongseokkang.harusaegim.dto.SignupRequest;
import io.github.myeongseokkang.harusaegim.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest req) {
        authService.signup(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
