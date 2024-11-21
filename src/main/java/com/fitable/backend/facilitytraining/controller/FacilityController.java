package com.fitable.backend.facilitytraining.controller;

import com.fitable.backend.facilitytraining.dto.FacilityItemNamesWithGptResponse;
import com.fitable.backend.facilitytraining.dto.FacilityResponse;
import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.dto.NaverBlogReviewResponse;
import com.fitable.backend.facilitytraining.service.FacilityService;
import com.fitable.backend.facilitytraining.service.NaverReviewService;
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

    @Autowired
    private NaverReviewService naverReviewService;

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

    /**
     * 네이버 블로그 리뷰 조회
     *
     * @param query   검색어
     * @param display 출력할 블로그 개수
     * @return 블로그 리뷰 리스트
     */
    @GetMapping
    public List<NaverBlogReviewResponse> getBlogReviews(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int display) {
        log.info("Received request to fetch blog reviews with query: {} and display: {}", query, display);

        // 네이버 리뷰 가져오기 서비스 호출
        List<NaverBlogReviewResponse> reviews = naverReviewService.fetchBlogReviews(query, display);

        log.info("Successfully fetched {} reviews", reviews.size());
        return reviews;
    }
}
