package com.fitable.backend.hometraining.service;

import com.fitable.backend.hometraining.dto.HomeTrainingResponse;
import com.fitable.backend.publicdata.entity.RecommendedExercise;
import com.fitable.backend.publicdata.repository.RecommendedExerciseRepository;
import com.fitable.backend.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeTrainingService {
    private final RecommendedExerciseRepository recommendedExerciseRepository;

    public HomeTrainingService(RecommendedExerciseRepository recommendedExerciseRepository){
        this.recommendedExerciseRepository = recommendedExerciseRepository;
    }


    public List<HomeTrainingResponse> getHomeTraining(User user) {
        String[] ageGroup = user.getAgeGroup().getDescription().split(" ");
        List<RecommendedExercise> homeTrainings;
        if(ageGroup.length==1) {
            homeTrainings = recommendedExerciseRepository.findByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(
                    user.getDisabilityType().getDescription(),
                    user.getDisabilityLevel().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0]);
        }
        else{
            homeTrainings = recommendedExerciseRepository.findByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(
                    user.getDisabilityType().getDescription(),
                    user.getDisabilityLevel().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0],
                    ageGroup[1]);
        }
        return homeTrainings.stream()
                .map(recommendedExercise -> new HomeTrainingResponse(
                        recommendedExercise.getRecommendedMovement(),
                        recommendedExercise.getMovementRank(),
                        recommendedExercise.getSportsStep()
                ))
                .collect(Collectors.toList());
    }
}
