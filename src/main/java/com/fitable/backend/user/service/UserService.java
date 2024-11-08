package com.fitable.backend.user.service;

import com.fitable.backend.user.dto.RegisterRequest;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    @Getter
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 메서드
    public void registerUser(RegisterRequest registerRequest) {
        if (existsByLoginId(registerRequest.getLoginId())) {
            log.warn("Registration failed. Login ID already exists: {}", registerRequest.getLoginId());
            throw new IllegalArgumentException("Login ID already exists");
        }

        User newUser = new User(registerRequest.getLoginId(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getAgeGroup(),
                registerRequest.getGender(),
                registerRequest.getDisabilityType(),
                registerRequest.getDisabilityLevel());

        userRepository.save(newUser);
        log.info("User registered successfully: {}", registerRequest.getLoginId());
    }

    // 사용자 존재 여부 확인
    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    // 사용자 로그인 ID로 조회
    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
