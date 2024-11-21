package com.fitable.backend.facilitytraining.service;

import com.fitable.backend.facilitytraining.dto.NaverBlogResponse;
import com.fitable.backend.facilitytraining.dto.NaverBlogReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class NaverReviewService {

    private final WebClient webClient;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public NaverReviewService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://openapi.naver.com/v1/search").build();
    }

    /**
     * 네이버 블로그 리뷰 가져오기
     *
     * @param query   검색어
     * @param display 가져올 블로그 리뷰 수
     * @return 블로그 리뷰 리스트
     */
    public List<NaverBlogReviewResponse> fetchBlogReviews(String query, int display) {

        if (clientId == null || clientSecret == null) {
            throw new IllegalStateException("Naver API credentials are not set");
        }

        // 네이버 블로그 API 호출 및 items 반환
        NaverBlogResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/blog.json")
                        .queryParam("query", query)
                        .queryParam("display", display)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverBlogResponse.class) // 응답을 NaverBlogResponse로 매핑
                .doOnError(e -> log.error("Error fetching reviews: {}", e.getMessage()))
                .block();

        // items 리스트 반환
        return response != null ? response.getItems() : List.of();
    }
}
