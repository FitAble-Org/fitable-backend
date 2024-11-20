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
    private final CustomUserDetailsService customUserDetailsService;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // 요청 URI와 헤더 로그
        log.info("Processing request for URI: {}", request.getRequestURI());
        log.info("Authorization Header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            log.debug("Extracted JWT: {}", jwt);
            try {
                final String username = jwtTokenUtil.extractUsername(jwt);
                log.info("Extracted username from JWT: {}", username);

                // SecurityContext에 인증 정보가 없는 경우 처리
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.debug("Loading user details for username: {}", username);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    log.debug("User details loaded: {}", userDetails);

                    if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.info("Authentication successful for user: {}", username);
                    } else {
                        log.warn("JWT validation failed for user: {}", username);
                    }
                }
            } catch (Exception e) {
                log.error("JWT validation failed: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
                return;
            }
        } else {
            log.warn("Authorization header missing or invalid: {}", authHeader);
        }

        // SecurityContext 확인
        log.debug("SecurityContext after filter: {}", SecurityContextHolder.getContext().getAuthentication());
        chain.doFilter(request, response);
    }
}
