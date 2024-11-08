package com.fitable.backend.publicdata.service;

import com.fitable.backend.publicdata.repository.RecommendedExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class RecommendedExerciseService {

    @Autowired
    private RecommendedExerciseRepository recommendedExerciseRepository;

    @Transactional
    public void saveAllExercises(List<RecommendedExercise> exercises) {
        recommendedExerciseRepository.saveAll(exercises);
    }
}
