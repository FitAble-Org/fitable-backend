package com.fitable.backend.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data // 추후에 제거
@Table(name = "users") // 테이블 이름 변경 : sql 예약어(user) 중복 방지
@EntityListeners(AuditingEntityListener.class) // 날짜 자동 업데이트를 위한 애노테이션
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String loginId;

    private String password;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private DisabilityType disabilityType;

    @Enumerated(EnumType.STRING)
    private DisabilityLevel disabilityLevel;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public User(String loginId, String password, AgeGroup ageGroup, Gender gender, DisabilityType disabilityType, DisabilityLevel disabilityLevel) {
        this.loginId = loginId;
        this.password = password;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.disabilityType = disabilityType;
        this.disabilityLevel = disabilityLevel;
    }

    // 나이
    public enum AgeGroup {
        TEENAGER, // 청소년 10대
        YOUNG_ADULT, // 청년 2~30대
        MIDDLE_AGED, // 중년 4~50대
        SENIOR // 장년 60대 이상
    }

    // 성별
    public enum Gender {
        FEMALE,
        MALE
    }

    // 장애 유형
    public enum DisabilityType {
        INTELLECTUAL, // 지적장애
        HEARING, // 청각장애
        VISION, // 시각장애
        SPINAL // 척수장애
    }

    // 장애 등급
    public enum DisabilityLevel {
        LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6, // 1~6등급
        PARTIAL_PARALYSIS, // 불완전마비
        COMPLETE_PARALYSIS // 완전마비
    }
}
