package com.fitable.backend.calendar.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarResponse {
    private long calendarId;
    private long exerciseId;
    private String exerciseName;
    private String exerciseType;
    private int duration;
    private LocalDate datePerformed ;

    public CalendarResponse(){}

    public CalendarResponse(Long calendarId, Long exerciseId, String exerciseName, String exerciseType, int duration, LocalDate datePerformed ) {
        this.calendarId = this.calendarId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.datePerformed  = LocalDate.from(datePerformed );
    }
}
