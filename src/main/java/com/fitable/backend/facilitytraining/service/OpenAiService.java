package com.fitable.backend.facilitytraining.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;  // JSON 파싱을 위한 ObjectMapper 추가

    public OpenAiService(@Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey) // 실제 API 키로 교체
                .build();
        this.objectMapper = new ObjectMapper();  // ObjectMapper 초기화
    }

    public Mono<String> getGptResponse(String prompt) {
        return webClient.post()
                .bodyValue(createRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractContent); // JSON 응답에서 content 필드만 추출
    }

    private Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("max_tokens", 300);
        requestBody.put("temperature", 0.8);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        requestBody.put("messages", List.of(message));
        return requestBody;
    }

    // JSON 응답에서 content 필드만 추출하는 메서드
    private String extractContent(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing content";
        }
    }
}
