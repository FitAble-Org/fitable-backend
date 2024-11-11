package com.fitable.backend.facilitytraining.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private final WebClient webClient;

    public OpenAiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer ") // 실제 API 키로 교체
                .build();
    }

    public Mono<String> getGptResponse(String prompt) {
        return webClient.post()
                .bodyValue(createRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class);
    }

    private Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4-turbo"); // 모델 선택
        requestBody.put("max_tokens", 300); // 원하는 토큰 수
        requestBody.put("temperature", 0.8); // 응답의 창의성

        // 챗 메시지 형식으로 prompt 전달
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "줄넘기, 탁구, 필라테스, 댄스, 볼링, 태권도, 수영 중에 사용자가 하나를 고를 수 있도록 네/아니오로 대답 가능한 질문 3개 한 문장씩만 만들어줘. 운동명을 언급하진 마.\n" +
                "예시) 1. 구기 종목을 하고 싶으신가요?\n" +
                "이후에 8가지 경우의 답변에 대해 매칭되는 운동명도 마지막에 보내줘. 8가지 경우에 대해 3개의 아니오(0)/네(1) 응답을 2진수로 만든 순서로 해서 아래처럼 운동명만!! 보내줘.\n" +
                "예시) 줄넘기/탁구/태권도/태권도/...");

        requestBody.put("messages", List.of(message));
        return requestBody;
    }
}
