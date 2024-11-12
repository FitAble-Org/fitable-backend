package com.fitable.backend.calendar.entity;

import com.fitable.backend.facilitytraining.entity.Facility;
import com.fitable.backend.hometraining.entity.RecommendedExercise;
import com.fitable.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_exercise_id", referencedColumnName = "id", nullable = true)
    private RecommendedExercise recommendedExercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", referencedColumnName = "id", nullable = true)
    private Facility facility;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    private int duration;

    @CreatedDate
    private LocalDate datePerformed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Calendar() {}

    public Calendar(RecommendedExercise recommendedExercise, ExerciseType exerciseType, int duration, User user) {
        this.recommendedExercise = recommendedExercise;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.user = user;
    }

    public Calendar(Facility facility, ExerciseType exerciseType, int duration, User user) {
        this.facility = facility;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.user = user;
    }

    @Getter
    public enum ExerciseType {
        HOME("가정운동"),
        OUTDOOR("외부운동");

        private final String description;

        ExerciseType(String description) {
            this.description = description;
        }

        public static ExerciseType fromDescription(String description) {
            for (ExerciseType type : ExerciseType.values()) {
                if (type.description.equals(description)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown description: " + description);
        }
    }
}
