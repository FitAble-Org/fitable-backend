package com.fitable.backend.user.controller;

import com.fitable.backend.user.dto.LoginRequest;
import com.fitable.backend.user.dto.ProfileUpdateRequest;
import com.fitable.backend.user.dto.RefreshTokenRequest;
import com.fitable.backend.user.dto.RegisterRequest;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.service.CustomUserDetailsService;
import com.fitable.backend.user.service.UserService;
import com.fitable.backend.user.util.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProfileUpdateRequest profileUpdateRequest){
        try{
            userService.updateUser(userDetails, profileUpdateRequest);
            return  ResponseEntity.status(HttpStatus.OK).body("User profile updated");
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());

        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, String> tokens = userService.login(loginRequest.getLoginId(), loginRequest.getPassword());

            // Access Token은 인증 헤더에 세팅
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokens.get("accessToken"));
            // Refresh Token은 쿠키에 세팅
            ResponseCookie refreshTokenCookie = createCookie("refreshToken", tokens.get("refreshToken"), 7 * 24 * 60 * 60);

            return ResponseEntity.ok()
                    .headers(headers)
                    .header("Set-Cookie", refreshTokenCookie.toString())
                    .body("Login successful");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // 쿠키 생성 메서드
    private ResponseCookie createCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
    }

    // 사용자 정보 조회 (테스트 코드, 추후 수정)
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            String username = jwtTokenUtil.extractUsername(refreshTokenRequest.getRefreshToken());

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(refreshTokenRequest.getRefreshToken(), userDetails)) {
                String newAccessToken = jwtTokenUtil.generateToken(userDetails);
                return ResponseEntity.ok(newAccessToken);  // 새 Access Token 문자열 반환
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error refreshing token");
        }
    }
}
