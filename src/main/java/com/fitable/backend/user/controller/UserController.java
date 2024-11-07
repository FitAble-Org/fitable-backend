package com.fitable.backend.user.controller;

import com.fitable.backend.user.dto.UserRequest;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
        // 로그인 아이디 중복 확인
        Optional<User> existingUser = userService.findByLoginId(userRequest.getLoginId());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Login ID already exists");
        }

        // 비밀번호가 비어있는 경우 예외 처리
        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }

        userService.registerUser(userRequest);
        return ResponseEntity.ok("User registered successfully");
    }
}
