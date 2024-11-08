package com.fitable.backend.homeTraining.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HomeTrainingResponse {
    private String exerciseName;
    private String recommendationLevel;
    private int duration;
    private LocalDate datePerformed ;

    public HomeTrainingResponse(String exerciseName, String recommendationLevel, int duration, LocalDate datePerformed ) {
        this.exerciseName = exerciseName;
        this.recommendationLevel = recommendationLevel;
        this.duration = duration;
        this.datePerformed  = LocalDate.from(datePerformed );
    }
}
