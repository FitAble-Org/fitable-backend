package com.fitable.backend.calendar.controller;

import com.fitable.backend.calendar.dto.AddCalendarRequest;
import com.fitable.backend.calendar.dto.CalendarResponse;
import com.fitable.backend.calendar.dto.UpdateCalendarRequest;
import com.fitable.backend.calendar.entity.Calendar;
import com.fitable.backend.calendar.service.CalendarService;
import com.fitable.backend.facilitytraining.entity.Facility;
import com.fitable.backend.facilitytraining.service.FacilityService;
import com.fitable.backend.hometraining.entity.RecommendedExercise;
import com.fitable.backend.hometraining.service.RecommendedExerciseService;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    private final RecommendedExerciseService recommendedExerciseService;
    private final FacilityService facilityService;
    private final UserService userService;

    @Autowired
    public CalendarController(CalendarService calendarService, RecommendedExerciseService recommendedExerciseService, FacilityService facilityService, UserService userService){
        this.calendarService = calendarService;
        this.recommendedExerciseService = recommendedExerciseService;
        this.facilityService = facilityService;
        this.userService = userService;
    }

    @PostMapping("/time")
    public ResponseEntity<String> updateCalendar(@RequestBody UpdateCalendarRequest request){
        calendarService.updateCalendar(request);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PostMapping
    public ResponseEntity<String> addCalendar(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddCalendarRequest request) {
        if(userDetails==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try{
            Optional<User> user = userService.findByLoginId(userDetails.getUsername());
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if(request.getExerciseType().equals(Calendar.ExerciseType.HOME.getDescription())){
                Optional<RecommendedExercise> recommendedExercise = recommendedExerciseService.getRecommendedExerciseById(request.getExerciseId());
                if(recommendedExercise.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }

                calendarService.addHomeTrainingCalendar(request, user.get(), recommendedExercise.get());
            }
            else if(request.getExerciseType().equals(Calendar.ExerciseType.OUTDOOR.getDescription())){
                Optional<Facility> facility = facilityService.getFacilityById(request.getExerciseId());
                if(facility.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }


                calendarService.addFacilityCalendar(request, user.get(), facility.get());
            }
            else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("운동 타입이 다릅니다.");
            }
            return new ResponseEntity<>("Calendar added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<CalendarResponse>> getCalendar(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("year") int year, @PathVariable("month") int month) {
        if(userDetails==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> user = userService.findByLoginId(userDetails.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CalendarResponse> CalendarList = calendarService.getCalendarsByMonth(year, month, user.get());
        return new ResponseEntity<>(CalendarList, HttpStatus.OK);
    }
}
