//package com.fitable.backend.publicdata;
//
//import com.fitable.backend.facilitytraining.entity.Facility;
//import com.fitable.backend.facilitytraining.service.FacilityService;
//import com.fitable.backend.hometraining.entity.RecommendedExercise;
//import com.fitable.backend.hometraining.service.RecommendedExerciseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//import com.opencsv.bean.CsvToBeanBuilder;
//
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Component
//public class DataLoader implements CommandLineRunner {
//
//    @Autowired
//    private FacilityService facilityService;
//
//    @Autowired
//    private RecommendedExerciseService recommendedExerciseService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 스포츠 시설 데이터 로드
//        Reader sportsFacilityReader = new InputStreamReader(new ClassPathResource("sports_facilities.csv").getInputStream());
//        List<Facility> facilities = new CsvToBeanBuilder<Facility>(sportsFacilityReader)
//                .withType(Facility.class)
//                .build()
//                .parse();
//        facilityService.saveAllFacilities(facilities);
//
//        // 스포츠 시설 데이터 로드2
//        Reader secondSportsFacilityReader = new InputStreamReader(new ClassPathResource("second_sports_facilities.csv").getInputStream());
//        List<Facility> secondFacilities = new CsvToBeanBuilder<Facility>(secondSportsFacilityReader)
//                .withType(Facility.class)
//                .build()
//                .parse();
//        facilityService.saveAllFacilities(secondFacilities);
//
//        // 추천 운동 데이터 로드
//        Reader recommendedExerciseReader = new InputStreamReader(new ClassPathResource("recommended_exercises.csv").getInputStream(), StandardCharsets.UTF_8);
//        List<RecommendedExercise> exercises = new CsvToBeanBuilder<RecommendedExercise>(recommendedExerciseReader)
//                .withType(RecommendedExercise.class)
//                .build()
//                .parse();
//        recommendedExerciseService.saveAllExercises(exercises);
//    }
//}
