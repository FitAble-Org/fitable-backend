package com.fitable.backend.facilitytraining.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityResponse {
    private Long id;
    private String fcltyNm;  // 시설명
    private String fcltyAddr;  // 시설주소
    private String itemNm;  // 종목명
    private String fcltyCourseSdivNm;  // 시설 강좌 구분명
    private Double fcltyXCrdntValue;  // 시설 X 좌표값
    private Double fcltyYCrdntValue;  // 시설 Y 좌표값
}
