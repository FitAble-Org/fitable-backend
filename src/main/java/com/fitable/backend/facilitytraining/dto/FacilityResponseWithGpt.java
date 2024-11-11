package com.fitable.backend.facilitytraining.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FacilityResponseWithGpt {
    private List<FacilityResponse> facilities;
    private String gptResponseContent;
}
