package com.fitable.backend.calendar.controller;

import com.fitable.backend.calendar.dto.AddCalendarRequest;
import com.fitable.backend.calendar.dto.CalendarResponse;
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
    private final CalendarService CalendarService;
    private final RecommendedExerciseService recommendedExerciseService;
    private final FacilityService facilityService;
    private final UserService userService;

    @Autowired
    public CalendarController(CalendarService CalendarService, RecommendedExerciseService recommendedExerciseService, FacilityService facilityService, UserService userService){
        this.CalendarService = CalendarService;
        this.recommendedExerciseService = recommendedExerciseService;
        this.facilityService = facilityService;
        this.userService = userService;
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

            if(request.getExerciseType()== Calendar.ExerciseType.HOME){
                Optional<RecommendedExercise> recommendedExercise = recommendedExerciseService.getRecommendedExerciseById(request.getExerciseId());
                if(recommendedExercise.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }

                CalendarService.addHomeTrainingCalendar(request, user.get(), recommendedExercise.get());
            }
            else if(request.getExerciseType()== Calendar.ExerciseType.OUTDOOR){
                Optional<Facility> facility = facilityService.getFacilityById(request.getExerciseId());
                if(facility.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }


                CalendarService.addFacilityCalendar(request, user.get(), facility.get());
            }
            return new ResponseEntity<>("Calendar added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{date}")
//  date: "yyyy-mm-dd"
    public ResponseEntity<List<CalendarResponse>> getCalendar(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if(userDetails==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> user = userService.findByLoginId(userDetails.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CalendarResponse> CalendarList = CalendarService.getCalendarsByDate(date, user.get());
        return new ResponseEntity<>(CalendarList, HttpStatus.OK);
    }
}
