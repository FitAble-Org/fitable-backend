package com.fitable.backend.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
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

    public User() {
    }

    public User(String loginId, String password, AgeGroup ageGroup, Gender gender, DisabilityType disabilityType, DisabilityLevel disabilityLevel) {
        this.loginId = loginId;
        this.password = password;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.disabilityType = disabilityType;
        this.disabilityLevel = disabilityLevel;
    }

    // 나이
    @Getter
    public enum AgeGroup {
        YOUNG_ADULT("10대 20대"), // 청년 10~20대
        MIDDLE_AGED("30대 40대"), // 중년 30~40대
        SENIOR("50대"); // 중장년 50대 이상

        private final String description;

        AgeGroup(String description) {
            this.description = description;
        }
    }

    // 성별
    @Getter
    public enum Gender {
        FEMALE("F"),
        MALE("M");

        private final String description;

        Gender(String description) {
            this.description = description;
        }

    }

    // 장애 유형
    @Getter
    public enum DisabilityType {
        INTELLECTUAL("지적장애"),
        HEARING("청각장애"),
        VISION("시각장애"),
        SPINAL("척수장애");

        private final String description;

        DisabilityType(String description) {
            this.description = description;
        }

    }

    // 장애 등급
    @Getter
    public enum DisabilityLevel {
        LEVEL_1("1등급"),
        LEVEL_2("2등급"),
        LEVEL_3("3등급"),
        LEVEL_4("4등급"),
        LEVEL_5("5등급"),
        LEVEL_6("6등급"),
        PARTIAL_PARALYSIS("불완전 마비"),
        COMPLETE_PARALYSIS("완전 마비");

        private final String description;

        DisabilityLevel(String description) {
            this.description = description;
        }
    }
}
