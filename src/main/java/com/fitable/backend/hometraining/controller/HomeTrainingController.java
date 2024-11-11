package com.fitable.backend.hometraining.controller;

import com.fitable.backend.hometraining.dto.HomeTrainingResponse;
import com.fitable.backend.hometraining.service.HomeTrainingService;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<HomeTrainingResponse>> getHomeTraining(@AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> user = userService.findByLoginId(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<HomeTrainingResponse> homeTrainingList = homeTrainingService.getHomeTraining(user.get());

        return new ResponseEntity<>(homeTrainingList, HttpStatus.OK);
    }
}
