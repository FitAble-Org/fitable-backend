package com.fitable.backend.calendar.dto;

import com.fitable.backend.calendar.entity.Calendar;
import lombok.Data;

@Data
public class AddCalendarRequest  {
    private long exerciseId;
    private int duration;
    private Calendar.ExerciseType exerciseType;
}
