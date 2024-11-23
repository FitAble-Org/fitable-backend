package com.fitable.backend.user.service;

import com.fitable.backend.user.dto.ProfileResponse;
import com.fitable.backend.user.dto.ProfileUpdateRequest;
import com.fitable.backend.user.dto.RegisterRequest;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.exception.UserNotFoundException;
import com.fitable.backend.user.mapper.UserMapper;
import com.fitable.backend.user.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper; // MyBatis Mapper 사용
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 회원 가입 메서드
    public void registerUser(RegisterRequest registerRequest) {
        if (existsByLoginId(registerRequest.getLoginId())) {
            log.warn("Registration failed. Login ID already exists: {}", registerRequest.getLoginId());
            throw new IllegalArgumentException("Login ID already exists");
        }

        User newUser = new User(
                registerRequest.getLoginId(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getAgeGroup(),
                registerRequest.getGender(),
                registerRequest.getDisabilityType(),
                registerRequest.getDisabilityLevel()
        );

        userMapper.registerUser(newUser); // MyBatis를 사용한 회원 가입
        log.info("User registered successfully: {}", registerRequest.getLoginId());
    }

    // 사용자 존재 여부 확인
    public boolean existsByLoginId(String loginId) {
        return userMapper.existsByLoginId(loginId); // MyBatis를 사용한 존재 여부 확인
    }

    public Map<String, String> login(String loginId, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

        // 사용자 유효성 검증
        validateUser(userDetails, password);

        // JWT 토큰 생성
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Redis에 리프레시 토큰 저장
        redisTemplate.opsForValue().set(
                "refreshToken:" + loginId,
                refreshToken,
                jwtTokenUtil.getRefreshTokenExpireTime(),
                TimeUnit.MILLISECONDS
        );

        log.info("JWT Token generated and stored in Redis for user: {}", loginId);

        // AccessToken과 RefreshToken을 Map으로 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    // 사용자 비밀번호 및 존재 여부 검증 메서드
    private void validateUser(UserDetails userDetails, String rawPassword) {
        if (userDetails == null) {
            log.warn("Login failed. User not found with login ID: {}", rawPassword);
            throw new UserNotFoundException("Login ID does not exist");
        }

        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            log.warn("Invalid password for user: {}", userDetails.getUsername());
            throw new RuntimeException("Invalid password");
        }
    }

    // 사용자 로그인 ID로 조회
    public Optional<User> findByLoginId(String loginId) {
        return Optional.ofNullable(userMapper.findByLoginId(loginId)); // MyBatis를 사용한 조회
    }

    public User getUserById(Long userId) {
        throw new UnsupportedOperationException("MyBatis 기반으로 ID 조회는 구현되지 않았습니다.");
    }

    public void updateUser(UserDetails userDetails, ProfileUpdateRequest request) {
        if (userDetails == null) throw new RuntimeException("User is not authenticated");

        int changed = userMapper.updateUserInfoByLoginId(
                userDetails.getUsername(),
                request.getAgeGroup(),
                request.getGender(),
                request.getDisabilityType(),
                request.getDisabilityLevel()
        );

        if (changed != 1) throw new RuntimeException("User not found.");
    }

    public ProfileResponse getUserProfile(UserDetails userDetails) {
        if (userDetails == null) throw new RuntimeException("User is not authenticated");

        User user = userMapper.findByLoginId(userDetails.getUsername());
        if (user == null) throw new RuntimeException("User not found.");

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setLoginId(user.getLoginId());
        profileResponse.setAgeGroup(user.getAgeGroup().getDescription());
        profileResponse.setGender(user.getGender().getDescription());
        profileResponse.setDisabilityType(user.getDisabilityType().getDescription());
        profileResponse.setDisabilityLevel(user.getDisabilityLevel().getDescription());

        return profileResponse;
    }

    // refreshToken 갱신
    public String refreshToken(String refreshToken) {
        try {
            String username = jwtTokenUtil.extractUsername(refreshToken);

            String storedRefreshToken = (String) redisTemplate.opsForValue().get("refreshToken:" + username);

            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                throw new RuntimeException("Invalid or expired refresh token");
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            return jwtTokenUtil.generateToken(userDetails);
        } catch (Exception e) {
            throw new RuntimeException("Error refreshing token");
        }
    }

    // 로그아웃
    public void logout(String loginId) {
        redisTemplate.delete("refreshToken:" + loginId);
    }
}
