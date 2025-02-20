package com.fitable.backend.calendar.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalendarResponse {
    private long calendarId;
    private long exerciseId;
    private String exerciseName;
    private String exerciseType;
    private int duration;
    private LocalDateTime datePerformed;

    public CalendarResponse(){}

    public CalendarResponse(Long calendarId, Long exerciseId, String exerciseName, String exerciseType, int duration, LocalDateTime datePerformed ) {
        this.calendarId = calendarId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.datePerformed  = LocalDateTime.from(datePerformed );
    }

    @Override
    public String toString() {
        return "CalendarResponse{" +
                "calendarId=" + calendarId +
                ", exerciseId=" + exerciseId +
                ", exerciseName='" + exerciseName + '\'' +
                ", exerciseType='" + exerciseType + '\'' +
                ", duration=" + duration +
                ", datePerformed=" + datePerformed +
                '}';
    }
}
