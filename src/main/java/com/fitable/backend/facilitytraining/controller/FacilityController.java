package com.fitable.backend.facilitytraining.controller;

import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.dto.FacilityResponse;
import com.fitable.backend.facilitytraining.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @PostMapping("/nearby")
    public List<FacilityResponse> getNearbyFacilities(@RequestBody LocationRequest locationRequest) {
        return facilityService.findFacilitiesWithinRadius(locationRequest);
    }
}
