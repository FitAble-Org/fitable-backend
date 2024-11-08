package com.fitable.backend.homeTraining.controller;

import com.fitable.backend.homeTraining.dto.AddHomeTrainingRequest;
import com.fitable.backend.homeTraining.dto.HomeTrainingResponse;
import com.fitable.backend.homeTraining.service.HomeTrainingService;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.service.UserService;
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

@RestController
@RequestMapping("api/home-training")
public class HomeTrainingController {
    private final HomeTrainingService homeTrainingService;
    private final UserService userService;
    @Autowired
    public HomeTrainingController(HomeTrainingService homeTrainingService, UserService userService){
        this.homeTrainingService = homeTrainingService;
        this.userService = userService;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addHomeTraining(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddHomeTrainingRequest request) {
        if(userDetails==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> user = userService.findByLoginId(userDetails.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try{
            homeTrainingService.addHomeTraining(request, user.get());
            return new ResponseEntity<>("HomeTraining added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{date}")
//  date: "yyyy-mm-dd"
    public ResponseEntity<List<HomeTrainingResponse>> getHomeTraining(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if(userDetails==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> user = userService.findByLoginId(userDetails.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<HomeTrainingResponse> homeTrainingList = homeTrainingService.getHomeTrainingsByDate(date, user.get());
        return new ResponseEntity<>(homeTrainingList, HttpStatus.OK);
    }
}
