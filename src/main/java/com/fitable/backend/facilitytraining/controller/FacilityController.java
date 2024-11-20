package com.fitable.backend.facilitytraining.controller;

import com.fitable.backend.facilitytraining.dto.FacilityItemNamesWithGptResponse;
import com.fitable.backend.facilitytraining.dto.FacilityResponse;
import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.service.FacilityService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@Slf4j
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @PostMapping("/nearby")
    public Mono<FacilityItemNamesWithGptResponse> getNearbyFacilities(@RequestBody LocationRequest locationRequest, HttpSession session) {
        // 요청 바디 로그
        log.info("Received /nearby request with payload: {}", locationRequest);

        // 세션 로그
        log.info("Session ID before processing /nearby: {}", session.getId());

        Mono<FacilityItemNamesWithGptResponse> response = facilityService.findFacilitiesWithinRadius(locationRequest, session);

        // 처리 결과 로그
        log.info("Session ID after processing /nearby: {}", session.getId());
        return response;
    }

    @GetMapping("/filter")
    public List<FacilityResponse> getFacilitiesByItemName(@RequestParam String itemName, HttpSession session) {
        // 요청 파라미터 로그
        log.info("Received /filter request with itemName: {}", itemName);

        // 세션 데이터 로그
        log.info("Session ID for /filter: {}", session.getId());
        List<FacilityResponse> facilityResponses = (List<FacilityResponse>) session.getAttribute("facilityResponses");

        if (facilityResponses == null) {
            log.error("No facility data found in session for /filter.");
            throw new IllegalStateException("근처 시설 데이터가 존재하지 않습니다. 먼저 /nearby 엔드포인트를 호출하십시오.");
        }

        // 필터링된 결과 로그
        List<FacilityResponse> filteredFacilities = facilityService.filterFacilitiesByItemName(facilityResponses, itemName);
        log.info("Filtered facilities count: {}", filteredFacilities.size());
        return filteredFacilities;
    }
}
