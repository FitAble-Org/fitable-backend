package com.fitable.backend.publicdata.repository;

import com.fitable.backend.publicdata.entity.RecommendedExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecommendedExerciseRepository extends JpaRepository<RecommendedExercise, Long> {
    List<RecommendedExercise> findByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(String troubleType, String troubleGrade, String genderCode, String age);
    @Query("SELECT e FROM RecommendedExercise e WHERE e.troubleType = :troubleType AND e.troubleGrade = :troubleGrade AND e.genderCode = :genderCode AND (e.age = :age1 OR e.age = :age2)")
    List<RecommendedExercise> findByTroubleTypeAndTroubleGradeAndGenderCodeAndAge(String troubleType, String troubleGrade, String genderCode, String age1, String age2);

}
