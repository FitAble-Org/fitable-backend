package com.fitable.backend.facilitytraining.controller;

import com.fitable.backend.facilitytraining.service.OpenAiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GptController {

    private final OpenAiService openAiService;

    public GptController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/api/gpt-response")
    public Mono<String> getGptResponse(@RequestParam String prompt) {
        return openAiService.getGptResponse(prompt);
    }
}
