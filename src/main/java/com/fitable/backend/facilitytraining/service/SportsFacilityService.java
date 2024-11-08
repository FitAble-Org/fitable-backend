package com.fitable.backend.facilitytraining.service;

import com.fitable.backend.facilitytraining.entity.SportsFacility;
import com.fitable.backend.facilitytraining.repository.SportsFacilityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportsFacilityService {

    @Autowired
    private SportsFacilityRepository sportsFacilityRepository;

    @Transactional
    public void saveAllFacilities(List<SportsFacility> facilities) {
        sportsFacilityRepository.saveAll(facilities);
    }
}
