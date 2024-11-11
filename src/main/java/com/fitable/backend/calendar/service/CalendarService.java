package com.fitable.backend.calendar.service;

import com.fitable.backend.calendar.dto.AddCalendarRequest;
import com.fitable.backend.calendar.dto.CalendarResponse;
import com.fitable.backend.calendar.entity.Calendar;
import com.fitable.backend.calendar.repository.CalendarRepository;
import com.fitable.backend.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final CalendarRepository CalendarRepository;

    public CalendarService(CalendarRepository CalendarRepository){
        this.CalendarRepository = CalendarRepository;
    }

    public void addCalendar(AddCalendarRequest addCalendarRequest, User user) {
        Calendar.RecommendationLevel recommendationLevel;

        try {
            recommendationLevel = Calendar.RecommendationLevel.valueOf(
                    addCalendarRequest.getRecommendationLevel().toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid recommendation level: " + addCalendarRequest.getRecommendationLevel());
        }

        Calendar Calendar = new Calendar(
                addCalendarRequest.getExerciseName(),
                recommendationLevel,
                addCalendarRequest.getDuration(),
                user
        );

        CalendarRepository.save(Calendar);
    }

    public List<CalendarResponse> getCalendarsByDate(LocalDate date, User user) {
        List<Calendar> Calendars = CalendarRepository.findByDatePerformedAndUser(date, user);
        return Calendars.stream()
                .map(Calendar -> new CalendarResponse(
                        Calendar.getExerciseName(),
                        Calendar.getRecommendationLevel().toString(),
                        Calendar.getDuration(),
                        Calendar.getDatePerformed()
                ))
                .collect(Collectors.toList());

    }
}
