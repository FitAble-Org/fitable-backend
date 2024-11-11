package com.fitable.backend.calendar.entity;

import com.fitable.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Data // 추후에 제거
@Table(name = "calendar")
@EntityListeners(AuditingEntityListener.class) // 날짜 자동 업데이트를 위한 애노테이션
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CalendarId;

    @Enumerated(EnumType.STRING)
    private RecommendationLevel recommendationLevel;

    private String exerciseName;

    private int duration;

    @CreatedDate
    private LocalDate datePerformed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Calendar() {}

    public Calendar(String exerciseName, RecommendationLevel recommendationLevel, int duration, User user) {
        this.exerciseName = exerciseName;
        this.recommendationLevel = recommendationLevel;
        this.duration = duration;
        this.user = user;
    }

    public enum RecommendationLevel {
        HIGH,
        MID,
        LOW
    }
}
