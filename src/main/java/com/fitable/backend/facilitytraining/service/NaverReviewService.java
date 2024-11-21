package com.fitable.backend.facilitytraining.service;

import com.fitable.backend.facilitytraining.dto.NaverBlogReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

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
        log.info("Fetching reviews for query: {}", query);

        log.info("Client ID: {}, Client Secret: {}", clientId, clientSecret); // 확인 로그 추가

        if (clientId == null || clientSecret == null) {
            throw new IllegalStateException("Naver API credentials are not set");
        }

        // 네이버 블로그 API 호출
        return List.of(Objects.requireNonNull(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/blog.json")
                        .queryParam("query", query)
                        .queryParam("display", display)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverBlogReviewResponse[].class)
                .doOnError(e -> log.error("Error fetching reviews: {}", e.getMessage()))
                .block()));
    }

}
