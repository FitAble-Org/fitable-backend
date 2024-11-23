package com.fitable.backend.calendar.service;

import com.fitable.backend.calendar.dto.AddCalendarRequest;
import com.fitable.backend.calendar.dto.CalendarResponse;
import com.fitable.backend.calendar.dto.UpdateCalendarRequest;
import com.fitable.backend.calendar.entity.Calendar;
import com.fitable.backend.calendar.repository.CalendarRepository;
import com.fitable.backend.facilitytraining.entity.Facility;
import com.fitable.backend.hometraining.entity.RecommendedExercise;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserService userService;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository, UserService userService){
        this.calendarRepository = calendarRepository;
        this.userService = userService;
    }

    public void addHomeTrainingCalendar(AddCalendarRequest request, User user, RecommendedExercise recommendedExercise) {
        Calendar calendar = new Calendar(
                recommendedExercise,
                Calendar.ExerciseType.fromDescription(request.getExerciseType()),
                request.getDuration(),
                user
        );
        calendarRepository.save(calendar);
    }

    public void addFacilityCalendar(AddCalendarRequest request, User user, Facility facility) {
        Calendar calendar = new Calendar(
                facility,
                Calendar.ExerciseType.fromDescription(request.getExerciseType()),
                request.getDuration(),
                user
        );
        calendarRepository.save(calendar);
    }

    public List<CalendarResponse> getCalendarsByMonth(int year, int month, User user) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1); // 해당 월의 첫째 날
        LocalDate endDate = yearMonth.atEndOfMonth(); // 해당 월의 마지막 날

        List<Calendar> calendars = calendarRepository.findByDatePerformedBetweenAndUser(startDate, endDate, user);
        return calendars.stream()
                .map(calendar -> {
                    CalendarResponse res = new CalendarResponse();
                    res.setCalendarId(calendar.getCalendarId());
                    res.setDatePerformed(calendar.getDatePerformed().toLocalDate());
                    res.setDuration(calendar.getDuration());
                    res.setExerciseType(calendar.getExerciseType().getDescription());
                    if (calendar.getExerciseType() == Calendar.ExerciseType.HOME) {
                        res.setExerciseName(calendar.getRecommendedExercise().getRecommendedMovement());
                        res.setExerciseId(calendar.getRecommendedExercise().getId());
                    } else if (calendar.getExerciseType() == Calendar.ExerciseType.OUTDOOR) {
                        res.setExerciseName(calendar.getFacility().getItemNm());
                        res.setExerciseId(calendar.getFacility().getId());
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }

    public List<CalendarResponse> getCalendarsByDate(LocalDate date, User user) {
        List<Calendar> calendar = calendarRepository.findByDatePerformedAndUser(date, user);
        return calendar.stream()
                .map(cal -> {
                    CalendarResponse res = new CalendarResponse();
                    res.setCalendarId(cal.getCalendarId());
                    res.setDatePerformed(cal.getDatePerformed().toLocalDate());
                    res.setDuration(cal.getDuration());
                    res.setExerciseType(cal.getExerciseType().getDescription());
                    if (cal.getExerciseType() == Calendar.ExerciseType.HOME) {
                        res.setExerciseName(cal.getRecommendedExercise().getRecommendedMovement());
                        res.setExerciseId(cal.getRecommendedExercise().getId());
                    } else if (cal.getExerciseType() == Calendar.ExerciseType.OUTDOOR) {
                        res.setExerciseName(cal.getFacility().getItemNm());
                        res.setExerciseId(cal.getFacility().getId());
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }

    public void updateCalendar(UpdateCalendarRequest request) {
        int changed = calendarRepository.updateDurationById(request.getId(), request.getDuration());
        if(changed!=1){
            throw new EntityNotFoundException();
        }


    }
}
