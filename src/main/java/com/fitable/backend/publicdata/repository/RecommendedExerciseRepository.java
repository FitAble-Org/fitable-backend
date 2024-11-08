package com.fitable.backend.publicdata.repository;

import com.fitable.backend.publicdata.entity.RecommendedExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedExerciseRepository extends JpaRepository<RecommendedExercise, Long> {
}
