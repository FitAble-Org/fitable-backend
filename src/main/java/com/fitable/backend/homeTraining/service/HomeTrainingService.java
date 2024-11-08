package com.fitable.backend.homeTraining.service;

import com.fitable.backend.homeTraining.dto.HomeTrainingResponse;
import com.fitable.backend.homeTraining.entity.HomeTraining;
import com.fitable.backend.homeTraining.dto.AddHomeTrainingRequest;
import com.fitable.backend.homeTraining.repository.HomeTrainingRepository;
import com.fitable.backend.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeTrainingService {
    private final HomeTrainingRepository homeTrainingRepository;

    public HomeTrainingService(HomeTrainingRepository homeTrainingRepository){
        this.homeTrainingRepository = homeTrainingRepository;
    }

    public void addHomeTraining(AddHomeTrainingRequest addHomeTrainingRequest, User user) {
        HomeTraining.RecommendationLevel recommendationLevel;

        try {
            recommendationLevel = HomeTraining.RecommendationLevel.valueOf(
                    addHomeTrainingRequest.getRecommendationLevel().toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid recommendation level: " + addHomeTrainingRequest.getRecommendationLevel());
        }

        HomeTraining homeTraining = new HomeTraining(
                addHomeTrainingRequest.getExerciseName(),
                recommendationLevel,
                addHomeTrainingRequest.getDuration(),
                user
        );

        homeTrainingRepository.save(homeTraining);
    }

    public List<HomeTrainingResponse> getHomeTrainingsByDate(LocalDate date, User user) {
        List<HomeTraining> homeTrainings = homeTrainingRepository.findByDatePerformedAndUser(date, user);
        return homeTrainings.stream()
                .map(homeTraining -> new HomeTrainingResponse(
                        homeTraining.getExerciseName(),
                        homeTraining.getRecommendationLevel().toString(),
                        homeTraining.getDuration(),
                        homeTraining.getDatePerformed()
                ))
                .collect(Collectors.toList());

    }
}
