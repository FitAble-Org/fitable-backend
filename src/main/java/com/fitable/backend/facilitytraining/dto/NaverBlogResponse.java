package com.fitable.backend.facilitytraining.dto;

import lombok.Data;
import java.util.List;

@Data
public class NaverBlogResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverBlogReviewResponse> items; // JSON의 "items" 배열
}