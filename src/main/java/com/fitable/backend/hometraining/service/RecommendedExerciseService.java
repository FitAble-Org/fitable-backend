package com.fitable.backend.hometraining.service;

import com.fitable.backend.hometraining.dto.RecommendedExerciseResponse;
import com.fitable.backend.hometraining.entity.RecommendedExercise;
import com.fitable.backend.hometraining.repository.RecommendedExerciseRepository;
import com.fitable.backend.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j

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
        log.info("유저 정보: {}", user.toString());
        log.info("연령대: {}", Arrays.toString(ageGroup));
        List<String> grades = getSurroundingGrades(user.getDisabilityLevel().getDescription(), user.getDisabilityType());
        List<RecommendedExercise> recommendedExercises;
        if(ageGroup.length==1) {
            recommendedExercises = recommendedExerciseRepository.distinctRecommendedExerciseByUser(
                    user.getDisabilityType().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0],
                    "60대",
                    grades);

        }
        else{
            recommendedExercises = recommendedExerciseRepository.distinctRecommendedExerciseByUser(
                    user.getDisabilityType().getDescription(),
                    user.getGender().getDescription(),
                    ageGroup[0],
                    ageGroup[1],
                    grades);
        }
        for(RecommendedExercise exercise: recommendedExercises){
            log.info("{}{}{}", exercise.getSportsStep(), exercise.getRecommendedMovement(), exercise.getMovementRank());

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

    public Optional<String> getInstructionByExerciseId(long exerciseId) {
            // 레포지토리 메서드를 호출하여 운동 설명을 조회
        return recommendedExerciseRepository.findInstructionByExerciseId(exerciseId);
    }

    public List<String> getSurroundingGrades(String grade, User.DisabilityType type) {
        // 등급 숫자를 추출 (예: "1등급" -> 1)
        // 결과 배열 생성
        List<String> result = new ArrayList<>();

        if(type==User.DisabilityType.SPINAL){
            result.add(grade);
            return result;
        }
        int currentGrade = Integer.parseInt(grade.replace("등급", ""));

        // 최소 및 최대 등급 설정
        int minGrade = Math.max(currentGrade - 1, 1); // 최소 1등급
        int maxGrade = Math.min(currentGrade + 1, 6); // 최대 6등급

        for (int i = minGrade; i <= maxGrade; i++) {
            result.add(i + "등급") ;
        }

        // 배열 반환
        return result;
    }
}
