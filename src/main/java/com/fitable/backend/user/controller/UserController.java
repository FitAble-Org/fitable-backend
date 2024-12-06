package com.fitable.backend.user.controller;

import com.fitable.backend.user.dto.*;
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

import java.util.HashMap;
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
                    // 디버깅용 액세스 토큰 반환! 추후 없앨 것
                    .body(tokens.get("accessToken") + "\n" + tokens.get("refreshToken"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // 액세스 토큰 갱신 (리프레쉬 토큰 이용)
    // 액세스 토큰 만료 시 401 응답하면 클라이언트가 해당 경로로 리프레쉬 토큰 보내서 갱신
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> tokenRequest) {
        try {
            String refreshToken = tokenRequest.get("refreshToken");
            String newAccessToken = userService.refreshToken(refreshToken);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProfileUpdateRequest profileUpdateRequest){
        try{
            userService.updateUser(userDetails, profileUpdateRequest);
            return  ResponseEntity.status(HttpStatus.OK).body("User profile updated");
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails){
        try{
            return ResponseEntity.ok().body(userService.getUserProfile(userDetails));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @GetMapping("/id")
    public ResponseEntity<String> getUserId(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails!=null){
            return ResponseEntity.ok().body(userDetails.getUsername());
        }
        else{
        return ResponseEntity.ok().body("");
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
    @GetMapping("/info/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
