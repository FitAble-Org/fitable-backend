package com.fitable.backend.user.service;

import com.fitable.backend.user.dto.UserRequest;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Getter
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRequest userRequest) {
        User user = new User();
        user.setLoginId(userRequest.getLoginId());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setAgeGroup(userRequest.getAgeGroup());
        user.setGender(userRequest.getGender());
        user.setDisabilityType(userRequest.getDisabilityType());
        user.setDisabilityLevel(userRequest.getDisabilityLevel());

        return userRepository.save(user);
    }
}
