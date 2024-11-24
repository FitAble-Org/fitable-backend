package com.fitable.backend.hometraining.service;

import com.fitable.backend.hometraining.dto.RecommendedExerciseResponse;
import com.fitable.backend.hometraining.entity.RecommendedExercise;
import com.fitable.backend.hometraining.mapper.RecommendedExerciseMapper;
import com.fitable.backend.hometraining.repository.RecommendedExerciseRepository;
import com.fitable.backend.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendedExerciseService {

    private final RecommendedExerciseMapper recommendedExerciseMapper;

    public RecommendedExerciseService(RecommendedExerciseMapper recommendedExerciseMapper) {
        this.recommendedExerciseMapper = recommendedExerciseMapper;
    }


    public Optional<RecommendedExercise> getRecommendedExerciseById(long id){
        return Optional.ofNullable(recommendedExerciseMapper.getRecommendedExerciseById(id));
    }

    public List<RecommendedExerciseResponse> getRecommendedExerciseByUserInfo(User user) {
        String[] ageGroup = user.getAgeGroup().getDescription().split(" ");
        List<RecommendedExercise> recommendedExercises;
        if (ageGroup.length == 1) {
            recommendedExercises = recommendedExerciseMapper.getRecommendedExerciseByTroubleTypeAndTroubleGradeAndGenderCodeAndSingleAge(
                    user.getDisabilityType().getDescription(),
                    user.getDisabilityLevel().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0]
            );
        } else {
            recommendedExercises = recommendedExerciseMapper.getRecommendedExerciseByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(
                    user.getDisabilityType().getDescription(),
                    user.getDisabilityLevel().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0],
                    ageGroup[1]
            );
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
        exercises.forEach(recommendedExerciseMapper::saveRecommendedExercise);
    }

    public Optional<String> getInstructionByExerciseId(long exerciseId) {
        return recommendedExerciseMapper.getInstructionByExerciseId(exerciseId);
    }
}
