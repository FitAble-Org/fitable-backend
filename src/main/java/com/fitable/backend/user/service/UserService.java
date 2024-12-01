package com.fitable.backend.user.service;

import com.fitable.backend.user.dto.ProfileResponse;
import com.fitable.backend.user.dto.ProfileUpdateRequest;
import com.fitable.backend.user.dto.RegisterRequest;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.exception.UserNotFoundException;
import com.fitable.backend.user.repository.UserRepository;
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

    private final UserRepository userRepository;
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

    public Map<String, String> login(String loginId, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

        // 사용자 유효성 검증
        validateUser(userDetails, password);

        // JWT 토큰 생성
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Redis에 리프레시 토큰 저장
        redisTemplate.opsForValue().set(
                "refreshToken:" + loginId, // Redis 키
                refreshToken,             // Redis 값
                jwtTokenUtil.getRefreshTokenExpireTime(), // 만료 시간
                TimeUnit.MILLISECONDS     // 시간 단위
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
        return userRepository.findByLoginId(loginId);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void updateUser(UserDetails userDetails, ProfileUpdateRequest request) {
        if(userDetails==null) throw new RuntimeException("User is not authenticated");

        int changed = userRepository.updateUserInfoByLoginId(
                userDetails.getUsername(),
                request.getAgeGroup(),
                request.getGender(),
                request.getDisabilityType(),
                request.getDisabilityLevel()
        );

        if(changed!=1)throw new RuntimeException("User not found.");
    }

    public ProfileResponse getUserProfile(UserDetails userDetails) {
        if(userDetails==null) throw new RuntimeException("User is not authenticated");

        Optional<User> userOpt = userRepository.findByLoginId(userDetails.getUsername());
        if(userOpt.isEmpty()) throw new RuntimeException("User not found.");
        User user = userOpt.get();

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
            // 토큰에서 사용자 이름 추출
            String username = jwtTokenUtil.extractUsername(refreshToken);

            // Redis에서 저장된 리프레시 토큰 가져오기
            String storedRefreshToken = (String) redisTemplate.opsForValue().get("refreshToken:" + username);

            // Redis에 저장된 토큰과 요청 토큰 비교
            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                throw new RuntimeException("Invalid or expired refresh token");
            }

            // 새 Access Token 생성
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtTokenUtil.generateToken(userDetails);

            return newAccessToken;
        } catch (Exception e) {
            throw new RuntimeException("Error refreshing token");
        }
    }

    // 로그아웃
    public void logout(String loginId) {
        redisTemplate.delete("refreshToken:" + loginId); // refreshToken 삭제
    }
}
