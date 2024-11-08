package com.fitable.backend.facilitytraining.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
    private double x;  // 현재 위치의 X 좌표 (경도)
    private double y;  // 현재 위치의 Y 좌표 (위도)
    private double radiusKm;  // 검색 반경 (킬로미터 단위)
}
