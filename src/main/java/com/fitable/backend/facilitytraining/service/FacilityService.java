package com.fitable.backend.facilitytraining.service;

import com.fitable.backend.facilitytraining.dto.FacilityItemsWithGptResponse;
import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.entity.Facility;
import com.fitable.backend.facilitytraining.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private OpenAiService openAiService;

    public Mono<FacilityItemsWithGptResponse> findFacilitiesWithinRadius(LocationRequest locationRequest) {
        double x = locationRequest.getX();
        double y = locationRequest.getY();
        double radiusKm = locationRequest.getRadiusKm();

        // 근처 시설 조회
        List<Facility> facilities = facilityRepository.findFacilitiesWithinRadius(x, y, radiusKm);

        // itemNm 값만 추출
        List<String> itemNames = facilities.stream()
                .map(Facility::getItemNm)
                .distinct()  // 중복 제거
                .collect(Collectors.toList());

        // itemNm 값들을 중복 없이 모아 프롬프트 생성
        String prompt = String.format("아래 요청사항에 맞게 예시)에 나온 형식대로 보내줘. 예시와 100%% 동일한 형식으로 보내. " +
                        "'%s' 중에 사용자가 하나를 고를 수 있도록 네/아니오로 대답 가능한 질문 3개 한 문장씩만 만들어줘. 운동명을 언급하진 마.\n" +
                        "이후에 8가지 경우의 답변에 대해 매칭되는 운동명도 마지막에 보내줘. 8가지 경우에 대해 3개의 아니오(0)/네(1) 응답을 2진수로 만든 순서로 해서 아래처럼 운동명만 보내줘.\n" +
                        "예시) 구기 종목을 하고 싶으신가요?\n 구기 종목을 하고 싶으신가요?\n 구기 종목을 하고 싶으신가요?\n 줄넘기/탁구/태권도/태권도/줄넘기/탁구/태권도/태권도/",
                String.join(", ", itemNames));

        // GPT 호출해 응답 받기
        Mono<String> gptResponse = openAiService.getGptResponse(prompt);

        return gptResponse.map(content -> new FacilityItemsWithGptResponse(itemNames, content));
    }
}
