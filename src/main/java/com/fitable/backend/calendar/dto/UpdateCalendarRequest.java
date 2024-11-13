package com.fitable.backend.calendar.dto;

import lombok.Data;

@Data
public class UpdateCalendarRequest {
    long id;
    int duration;

    public UpdateCalendarRequest(long id, int duration) {
        this.id = id;
        this.duration = duration;
    }
}
