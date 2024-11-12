package com.fitable.backend.calendar.service;

import com.fitable.backend.calendar.dto.AddCalendarRequest;
import com.fitable.backend.calendar.dto.CalendarResponse;
import com.fitable.backend.calendar.entity.Calendar;
import com.fitable.backend.calendar.repository.CalendarRepository;
import com.fitable.backend.facilitytraining.entity.Facility;
import com.fitable.backend.hometraining.entity.RecommendedExercise;
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

    public void addHomeTrainingCalendar(AddCalendarRequest request, User user, RecommendedExercise recommendedExercise) {

        Calendar Calendar = new Calendar(
                recommendedExercise,
                request.getExerciseType(),
                request.getDuration(),
                user
        );

        CalendarRepository.save(Calendar);
    }

    public void addFacilityCalendar(AddCalendarRequest request, User user, Facility facility) {
        Calendar Calendar = new Calendar(
                facility,
                request.getExerciseType(),
                request.getDuration(),
                user
        );

        CalendarRepository.save(Calendar);
    }

    public List<CalendarResponse> getCalendarsByDate(LocalDate date, User user) {
        List<Calendar> Calendars = CalendarRepository.findByDatePerformedAndUser(date, user);
        return Calendars.stream()
                .map(calendar -> {
                    CalendarResponse res = new CalendarResponse();
                    res.setCalendarId(calendar.getCalendarId());
                    res.setDatePerformed(calendar.getDatePerformed());
                    res.setDuration(calendar.getDuration());
                    res.setExerciseType(calendar.getExerciseType().getDescription());
                    if(calendar.getExerciseType()==Calendar.ExerciseType.HOME){
                        res.setExerciseName(calendar.getRecommendedExercise().getRecommendedMovement());
                        res.setExerciseId(calendar.getRecommendedExercise().getId());
                    }
                    else if(calendar.getExerciseType()==Calendar.ExerciseType.OUTDOOR){
                        res.setExerciseName(calendar.getFacility().getItemNm());
                        res.setExerciseId(calendar.getFacility().getId());
                    }
                    return res;
                }
                )
                .collect(Collectors.toList());

    }
}
