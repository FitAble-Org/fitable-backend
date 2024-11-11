package com.fitable.backend.calendar.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarResponse {
    private String exerciseName;
    private String recommendationLevel;
    private int duration;
    private LocalDate datePerformed ;

    public CalendarResponse(String exerciseName, String recommendationLevel, int duration, LocalDate datePerformed ) {
        this.exerciseName = exerciseName;
        this.recommendationLevel = recommendationLevel;
        this.duration = duration;
        this.datePerformed  = LocalDate.from(datePerformed );
    }
}
