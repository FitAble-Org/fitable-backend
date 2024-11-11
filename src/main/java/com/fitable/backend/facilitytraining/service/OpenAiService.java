package com.fitable.backend.facilitytraining.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService {

    private final WebClient webClient;

    public OpenAiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/completions")
                .defaultHeader("Authorization", "Bearer ") // API 키 설정
                .build();
    }

    public Mono<String> getGptResponse(String prompt) {
        return webClient.post()
                .bodyValue(createRequestBody("줄넘기, 탁구, 필라테스, 댄스, 볼링, 태권도, 수영 중에 사용자가 하나를 고를 수 있도록 네/아니오로 대답 가능한 질문 3개 한 문장씩만 만들어줘. 운동명을 언급하진 마." +
                        "예시) 1. 구기 종목을 하고 싶으신가요?" +
                        "이후에 8가지 경우의 답변에 대해 매칭되는 운동도 마지막에 보내줘. 8가지 경우에 대해 아래처럼 운동명만! 보내줘." +
                        "예시) 줄넘기(아니오,아니오,아니오)/탁구(아니오,아니오,네)/태권도/태권도/..."))
                .retrieve()
                .bodyToMono(String.class);
    }

    private Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo-instruct"); // 모델 선택
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 300); // 원하는 토큰 수
        requestBody.put("temperature", 0.8); // 응답의 창의성
        return requestBody;
    }
}
