package com.fitable.backend.publicdata.service;

import com.fitable.backend.publicdata.entity.SportsFacility;
import com.fitable.backend.publicdata.repository.SportsFacilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
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
