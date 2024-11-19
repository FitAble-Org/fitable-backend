package com.fitable.backend.filter;

import com.fitable.backend.user.service.CustomUserDetailsService;
import com.fitable.backend.user.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService; // UserService 대신 CustomUserDetailsService 사용

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // 요청 URI와 Authorization 헤더 로깅
        log.info("Processing request for URI: {}", request.getRequestURI());
        log.info("Authorization Header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            try {
                // JWT에서 사용자 이름 추출
                final String username = jwtTokenUtil.extractUsername(jwt);
                log.info("Extracted username from JWT: {}", username);

                // SecurityContext에 인증 정보가 없는 경우 설정
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        log.info("JWT validation successful for user: {}", username);
                    }
                }
            } catch (Exception e) {
                log.error("JWT validation failed: {}", e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }

}
