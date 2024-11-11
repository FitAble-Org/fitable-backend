package com.fitable.backend.calendar.dto;

import lombok.Data;

@Data
public class AddCalendarRequest  {
    private String exerciseName;
    private String recommendationLevel;
    private int duration;
}
