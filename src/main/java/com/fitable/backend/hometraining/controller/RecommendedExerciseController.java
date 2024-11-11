package com.fitable.backend.hometraining.controller;

import com.fitable.backend.hometraining.dto.RecommendedExerciseResponse;
import com.fitable.backend.hometraining.service.RecommendedExerciseService;
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
public class RecommendedExerciseController {
    private final RecommendedExerciseService recommendedExerciseService;
    private final UserService userService;
    @Autowired
    public RecommendedExerciseController(RecommendedExerciseService recommendedExerciseService, UserService userService){
        this.recommendedExerciseService = recommendedExerciseService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<RecommendedExerciseResponse>> getRecommendedExercise(@AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> user = userService.findByLoginId(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<RecommendedExerciseResponse> RecommendedExerciseList = recommendedExerciseService.getRecommendedExercise(user.get());

        return new ResponseEntity<>(RecommendedExerciseList, HttpStatus.OK);
    }
}
