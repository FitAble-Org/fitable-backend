package com.fitable.backend.facilitytraining.dto;

import lombok.Data;

@Data
public class NaverBlogReviewResponse {
    private String title;       // 블로그 포스트 제목
    private String link;        // 블로그 포스트 URL
    private String description; // 요약된 내용
    private String bloggername; // 블로그 이름
    private String bloggerlink; // 블로그 주소
    private String postdate;    // 작성 날짜
}