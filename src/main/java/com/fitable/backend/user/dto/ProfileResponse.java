package com.fitable.backend.user.dto;

import com.fitable.backend.user.entity.User;
import lombok.Data;

@Data
public class ProfileResponse {
    private String loginId;
    private String ageGroup; // 청소년, 청년, 중년, 장년
    private String gender; // 여성, 남성
    private String disabilityType; // 지적장애, 청각장애, 시각장애, 척수장애
    private String disabilityLevel; // 1등급 ~ 6등급, 불완전마비, 완전마비
}
