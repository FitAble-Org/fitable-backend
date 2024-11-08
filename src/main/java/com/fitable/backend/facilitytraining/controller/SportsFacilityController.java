package com.fitable.backend.facilitytraining.controller;

import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.dto.SportsFacilityResponse;
import com.fitable.backend.facilitytraining.service.SportsFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class SportsFacilityController {

    @Autowired
    private SportsFacilityService sportsFacilityService;

    @PostMapping("/nearby")
    public List<SportsFacilityResponse> getNearbyFacilities(@RequestBody LocationRequest locationRequest) {
        return sportsFacilityService.findFacilitiesWithinRadius(locationRequest);
    }
}
