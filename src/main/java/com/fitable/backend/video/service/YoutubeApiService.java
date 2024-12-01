package com.fitable.backend.video.service;

import com.fitable.backend.video.dto.YouTubeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class YoutubeApiService {
    @Value("${youtube.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public YoutubeApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/youtube/v3").build();
    }

    public List<String> fetchVideosFromYouTube(String title) {
        String endpoint = "/search?part=snippet&type=video&q=" + "국민체력100" + title
                + "&type=video&maxResults=1&key=" + apiKey;

        try {
            // 비동기로 API 호출 및 응답 처리
            Mono<YouTubeResponse> responseMono = webClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .bodyToMono(YouTubeResponse.class);

            // 결과 동기화 및 처리
            YouTubeResponse response = responseMono.block();

            if (response != null && response.getItems() != null) {
                // 비디오 URL 리스트 생성
                List<String> videoUrls = new ArrayList<>();
                response.getItems().forEach(item -> {
                    String videoId = item.getId().getVideoId();
                    if (videoId != null) {
                        videoUrls.add("https://www.youtube.com/watch?v=" + videoId);
                    }
                });
                return videoUrls;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 데이터가 없을 경우 빈 리스트 반환
        return new ArrayList<>();
    }
}
