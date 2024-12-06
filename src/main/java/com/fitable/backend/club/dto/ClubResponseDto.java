package com.fitable.backend.club.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClubResponseDto {
    private Integer clubId;
    private String itemNm;
    private String subitemNm;
    private String region;
    private String signguNm;
    private String clubNm;
    private String disabilityType;
    private String operTimeCn;
    private String clubIntrcnCn;
}
