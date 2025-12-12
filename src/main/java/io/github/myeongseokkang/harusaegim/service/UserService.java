package io.github.myeongseokkang.harusaegim.service;

import io.github.myeongseokkang.harusaegim.dto.UpdateUserRequest;
import io.github.myeongseokkang.harusaegim.dto.UserResponse;
import io.github.myeongseokkang.harusaegim.entity.User;
import io.github.myeongseokkang.harusaegim.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public UserResponse get(Long id) {
        return UserResponse.from(userRepository.findById(id).orElseThrow());
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest req) {
        User u = userRepository.findById(id).orElseThrow();
        u.setDisplayName(req.getDisplayName());
        return UserResponse.from(u);
    }
}
