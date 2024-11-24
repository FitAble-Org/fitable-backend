package com.fitable.backend.hometraining.mapper;

import com.fitable.backend.hometraining.entity.RecommendedExercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RecommendedExerciseMapper {

    RecommendedExercise getRecommendedExerciseById(@Param("id") long id);

    List<RecommendedExercise> getRecommendedExerciseByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(
            @Param("troubleType") String troubleType,
            @Param("troubleGrade") String troubleGrade,
            @Param("genderCode") String genderCode,
            @Param("age") String age1,
            @Param("age2") String age2
    );

    List<RecommendedExercise> getRecommendedExerciseByTroubleTypeAndTroubleGradeAndGenderCodeAndSingleAge(
            @Param("troubleType") String troubleType,
            @Param("troubleGrade") String troubleGrade,
            @Param("genderCode") String genderCode,
            @Param("age") String age
    );

    void saveRecommendedExercise(RecommendedExercise recommendedExercise);

    Optional<String> getInstructionByExerciseId(@Param("exerciseId") long exerciseId);
}
