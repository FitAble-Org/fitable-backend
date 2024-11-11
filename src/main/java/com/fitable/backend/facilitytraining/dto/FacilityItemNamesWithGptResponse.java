package com.fitable.backend.facilitytraining.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FacilityItemNamesWithGptResponse {
    private List<String> itemNames;  // itemNm만 포함하는 리스트
    private String gptResponseContent;
}
