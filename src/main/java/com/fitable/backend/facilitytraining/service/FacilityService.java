package com.fitable.backend.facilitytraining.service;

import com.fitable.backend.facilitytraining.dto.FacilityItemNamesWithGptResponse;
import com.fitable.backend.facilitytraining.dto.FacilityResponse;
import com.fitable.backend.facilitytraining.dto.LocationRequest;
import com.fitable.backend.facilitytraining.entity.Facility;
import com.fitable.backend.facilitytraining.repository.FacilityRepository;
import jakarta.servlet.http.HttpSession;
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

    public Mono<FacilityItemNamesWithGptResponse> findFacilitiesWithinRadius(LocationRequest locationRequest, HttpSession session) {
        double x = locationRequest.getX();
        double y = locationRequest.getY();
        double radiusKm = locationRequest.getRadiusKm();

        // 검색된 시설 정보를 FacilityResponse 리스트로 변환
        List<Facility> facilities = facilityRepository.findFacilitiesWithinRadius(x, y, radiusKm);
        List<FacilityResponse> facilityResponses = facilities.stream().map(facility -> {
            FacilityResponse response = new FacilityResponse();
            response.setId(facility.getId());
            response.setFcltyNm(facility.getFcltyNm());
            response.setFcltyAddr(facility.getFcltyAddr());
            response.setItemNm(facility.getItemNm());
            response.setFcltyCourseSdivNm(facility.getFcltyCourseSdivNm());
            response.setFcltyXCrdntValue(facility.getFcltyXCrdntValue());
            response.setFcltyYCrdntValue(facility.getFcltyYCrdntValue());
            return response;
        }).collect(Collectors.toList());

        // 세션에 전체 시설 정보 저장
        session.setAttribute("facilityResponses", facilityResponses);

        // itemNm 필드만 추출하여 중복을 제거한 후 리스트로 변환
        List<String> itemNames = facilityResponses.stream()
                .map(FacilityResponse::getItemNm)
                .distinct()
                .collect(Collectors.toList());

        // GPT를 호출할 프롬프트 생성
        String prompt = String.format("아래 요청사항에 맞게 예시)에 나온 형식대로 보내줘. 예시와 100%% 동일한 형식으로 보내. " +
                        "'%s' 중에 사용자가 하나를 고를 수 있도록 네/아니오로 대답 가능한 질문 3개 한 문장씩만 만들어줘. 운동명을 언급하진 마.\n" +
                        "이후에 8가지 경우의 답변에 대해 매칭되는 운동명도 마지막에 보내줘. 8가지 경우에 대해 3개의 아니오(0)/네(1) 응답을 2진수로 만든 순서로 해서 아래처럼 운동명만 보내줘.\n" +
                        "예시) 구기 종목을 하고 싶으신가요?\n 구기 종목을 하고 싶으신가요?\n 구기 종목을 하고 싶으신가요?\n 줄넘기/탁구/태권도/태권도/줄넘기/탁구/태권도/태권도/",
                String.join(", ", itemNames));

        // GPT 호출해 응답 받기
        Mono<String> gptResponse = openAiService.getGptResponse(prompt);

        // GPT 응답과 itemNames 리스트를 포함하는 FacilityItemNamesWithGptResponse 반환
        return gptResponse.map(content -> new FacilityItemNamesWithGptResponse(itemNames, content));
    }

    public List<FacilityResponse> filterFacilitiesByItemName(List<FacilityResponse> facilityResponses, String itemName) {
        if (facilityResponses == null) return List.of();

        return facilityResponses.stream()
                .filter(response -> response.getItemNm().equals(itemName))
                .collect(Collectors.toList());
    }
}
