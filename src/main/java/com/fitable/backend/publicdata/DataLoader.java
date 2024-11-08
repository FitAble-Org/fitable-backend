package com.fitable.backend.publicdata;

import com.fitable.backend.publicdata.entity.RecommendedExercise;
import com.fitable.backend.publicdata.entity.SportsFacility;
import com.fitable.backend.publicdata.service.RecommendedExerciseService;
import com.fitable.backend.publicdata.service.SportsFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SportsFacilityService sportsFacilityService;

    @Autowired
    private RecommendedExerciseService recommendedExerciseService;

    @Override
    public void run(String... args) throws Exception {
        // 스포츠 시설 데이터 로드
        Reader sportsFacilityReader = new InputStreamReader(new ClassPathResource("sports_facilities.csv").getInputStream());
        List<SportsFacility> facilities = new CsvToBeanBuilder<SportsFacility>(sportsFacilityReader)
                .withType(SportsFacility.class)
                .build()
                .parse();
        sportsFacilityService.saveAllFacilities(facilities);

        // 스포츠 시설 데이터 로드2
        Reader secondSportsFacilityReader = new InputStreamReader(new ClassPathResource("second_sports_facilities.csv").getInputStream());
        List<SportsFacility> secondFacilities = new CsvToBeanBuilder<SportsFacility>(secondSportsFacilityReader)
                .withType(SportsFacility.class)
                .build()
                .parse();
        sportsFacilityService.saveAllFacilities(secondFacilities);

        // 추천 운동 데이터 로드
        Reader recommendedExerciseReader = new InputStreamReader(new ClassPathResource("recommended_exercises.csv").getInputStream());
        List<RecommendedExercise> exercises = new CsvToBeanBuilder<RecommendedExercise>(recommendedExerciseReader)
                .withType(RecommendedExercise.class)
                .build()
                .parse();
        recommendedExerciseService.saveAllExercises(exercises);
    }
}
