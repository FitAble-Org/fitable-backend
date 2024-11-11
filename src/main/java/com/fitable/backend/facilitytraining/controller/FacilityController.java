package com.fitable.backend.facilitytraining.controller;

import com.fitable.backend.facilitytraining.dto.FacilityItemNamesWithGptResponse;
import com.fitable.backend.facilitytraining.dto.FacilityResponse;
import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.service.FacilityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @PostMapping("/nearby")
    public Mono<FacilityItemNamesWithGptResponse> getNearbyFacilities(@RequestBody LocationRequest locationRequest, HttpSession session) {
        return facilityService.findFacilitiesWithinRadius(locationRequest, session);
    }

    @GetMapping("/filter")
    public List<FacilityResponse> getFacilitiesByItemName(@RequestParam String itemName, HttpSession session) {
        // 세션에서 facilityResponses 가져오기
        List<FacilityResponse> facilityResponses = (List<FacilityResponse>) session.getAttribute("facilityResponses");

        if (facilityResponses == null) {
            throw new IllegalStateException("근처 시설 데이터가 존재하지 않습니다. 먼저 /nearby 엔드포인트를 호출하십시오.");
        }

        // 세션의 데이터에서 itemName으로 필터링하여 반환
        return facilityService.filterFacilitiesByItemName(facilityResponses, itemName);
    }
}
