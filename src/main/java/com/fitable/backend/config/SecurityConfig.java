package com.fitable.backend.config;

import com.fitable.backend.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        // CORS 사전 요청(OPTIONS)은 인증 없이 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 로그인 및 회원가입 경로는 인증 없이 접근 가능
                        .requestMatchers("/api/users/login", "/api/users/register", "/error", "/").permitAll()
                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .securityContext(securityContext -> securityContext.requireExplicitSave(false)) //익명 인증 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless 모드 설정
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 자격 증명 허용
        config.setAllowedOriginPatterns(Arrays.asList(
                "https://fitable.kro.kr",      // 프론트엔드 도메인
                "https://api.fitable.kro.kr",  // API 도메인
                "http://43.201.248.185" //테스트용 IP
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        config.setExposedHeaders(Arrays.asList("Authorization"));  // 노출 헤더 추가
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
}
