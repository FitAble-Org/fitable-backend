package com.fitable.backend.facilitytraining.service;

import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.dto.FacilityResponse;
import com.fitable.backend.facilitytraining.entity.Facility;
import com.fitable.backend.facilitytraining.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Transactional
    public void saveAllFacilities(List<Facility> facilities) {
        facilityRepository.saveAll(facilities);
    }

    public List<FacilityResponse> findFacilitiesWithinRadius(LocationRequest locationRequest) {
        double x = locationRequest.getX();
        double y = locationRequest.getY();
        double radiusKm = locationRequest.getRadiusKm();

        List<Facility> facilities = facilityRepository.findFacilitiesWithinRadius(x, y, radiusKm);

        return facilities.stream().map(facility -> {
            FacilityResponse response = new FacilityResponse();
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
