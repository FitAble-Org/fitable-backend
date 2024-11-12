package com.fitable.backend.hometraining.service;

import com.fitable.backend.hometraining.dto.RecommendedExerciseResponse;
import com.fitable.backend.hometraining.entity.RecommendedExercise;
import com.fitable.backend.hometraining.repository.RecommendedExerciseRepository;
import com.fitable.backend.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendedExerciseService {

    private final RecommendedExerciseRepository recommendedExerciseRepository;

    public RecommendedExerciseService(RecommendedExerciseRepository recommendedExerciseRepository){
        this.recommendedExerciseRepository = recommendedExerciseRepository;
    }

    public Optional<RecommendedExercise> getRecommendedExerciseById(long id){
        return recommendedExerciseRepository.findById(id);
    }

    public List<RecommendedExerciseResponse> getRecommendedExerciseByUserInfo(User user) {
        String[] ageGroup = user.getAgeGroup().getDescription().split(" ");
        List<RecommendedExercise> recommendedExercises;
        if(ageGroup.length==1) {
            recommendedExercises = recommendedExerciseRepository.findByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(
                    user.getDisabilityType().getDescription(),
                    user.getDisabilityLevel().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0]);
        }
        else{
            recommendedExercises = recommendedExerciseRepository.findByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(
                    user.getDisabilityType().getDescription(),
                    user.getDisabilityLevel().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0],
                    ageGroup[1]);
        }
        return recommendedExercises.stream()
                .map(recommendedExercise -> new RecommendedExerciseResponse(
                        recommendedExercise.getId(),
                        recommendedExercise.getRecommendedMovement(),
                        recommendedExercise.getMovementRank(),
                        recommendedExercise.getSportsStep()
                ))
                .collect(Collectors.toList());
    }

    public void saveAllExercises(List<RecommendedExercise> exercises) {
        recommendedExerciseRepository.saveAll(exercises);
    }
}
