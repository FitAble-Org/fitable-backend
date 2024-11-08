package com.fitable.backend.facilitytraining.service;

import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.dto.SportsFacilityResponse;
import com.fitable.backend.facilitytraining.entity.SportsFacility;
import com.fitable.backend.facilitytraining.repository.SportsFacilityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SportsFacilityService {

    @Autowired
    private SportsFacilityRepository sportsFacilityRepository;

    @Transactional
    public void saveAllFacilities(List<SportsFacility> facilities) {
        sportsFacilityRepository.saveAll(facilities);
    }

    public List<SportsFacilityResponse> findFacilitiesWithinRadius(LocationRequest locationRequest) {
        double x = locationRequest.getX();
        double y = locationRequest.getY();
        double radiusKm = locationRequest.getRadiusKm();

        List<SportsFacility> facilities = sportsFacilityRepository.findFacilitiesWithinRadius(x, y, radiusKm);

        return facilities.stream().map(facility -> {
            SportsFacilityResponse response = new SportsFacilityResponse();
            response.setId(facility.getId());
            response.setFcltyNm(facility.getFcltyNm());
            response.setFcltyAddr(facility.getFcltyAddr());
            response.setItemNm(facility.getItemNm());
            response.setFcltyCourseSdivNm(facility.getFcltyCourseSdivNm());
            response.setFcltyXCrdntValue(facility.getFcltyXCrdntValue());
            response.setFcltyYCrdntValue(facility.getFcltyYCrdntValue());
            return response;
        }).collect(Collectors.toList());
    }
}
