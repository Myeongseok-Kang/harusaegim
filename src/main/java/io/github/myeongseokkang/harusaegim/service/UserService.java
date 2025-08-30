package io.github.myeongseokkang.harusaegim.service;

import io.github.myeongseokkang.harusaegim.entity.User;
import io.github.myeongseokkang.harusaegim.dto.UpdateUserRequest;
import io.github.myeongseokkang.harusaegim.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public User get(Long id) { return userRepository.findById(id).orElseThrow(); }

    @Transactional
    public User update(Long id, UpdateUserRequest req) {
        User u = userRepository.findById(id).orElseThrow();
        u.setDisplayName(req.getDisplayName());
        return u;
    }
}
