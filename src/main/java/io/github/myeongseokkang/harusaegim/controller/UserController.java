package io.github.myeongseokkang.harusaegim.controller;

import io.github.myeongseokkang.harusaegim.entity.User;
import io.github.myeongseokkang.harusaegim.dto.UpdateUserRequest;
import io.github.myeongseokkang.harusaegim.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/me")
    public ResponseEntity<User> me(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(userService.get(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<User> update(@RequestAttribute("userId") Long userId, @Valid @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(userService.update(userId, req));
    }
}
