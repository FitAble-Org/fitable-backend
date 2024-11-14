package com.fitable.backend.user.dto;

import com.fitable.backend.user.entity.User;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private User.AgeGroup ageGroup; // 청소년, 청년, 중년, 장년
    private User.Gender gender; // 여성, 남성
    private User.DisabilityType disabilityType; // 지적장애, 청각장애, 시각장애, 척수장애
    private User.DisabilityLevel disabilityLevel; // 1등급 ~ 6등급, 불완전마비, 완전마비
}
