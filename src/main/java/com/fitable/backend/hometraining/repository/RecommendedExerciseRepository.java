package com.fitable.backend.hometraining.repository;

import com.fitable.backend.hometraining.entity.RecommendedExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendedExerciseRepository extends JpaRepository<RecommendedExercise, Long> {
    @Query("SELECT e " +
            "FROM RecommendedExercise e " +
            "WHERE e.troubleType = :troubleType " +
            "AND e.genderCode = :genderCode " +
            "AND (e.age = :age1 OR e.age = :age2) " +
            "AND e.troubleGrade IN (:troubleGrades) " +
            "GROUP BY e.recommendedMovement")
    List<RecommendedExercise> distinctRecommendedExerciseByUser(
            @Param("troubleType") String troubleType,
            @Param("genderCode") String genderCode,
            @Param("age1") String age1,
            @Param("age2") String age2,
            @Param("troubleGrades") List<String> troubleGrades);

    @Query("SELECT e.movementInstructions FROM RecommendedExercise e WHERE e.id = :exerciseId")
    Optional<String> findInstructionByExerciseId(long exerciseId);
}
