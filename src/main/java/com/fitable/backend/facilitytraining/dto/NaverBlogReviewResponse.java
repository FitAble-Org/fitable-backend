package com.fitable.backend.facilitytraining.dto;

import lombok.Data;

@Data
public class NaverBlogReviewResponse {
    private String title;        // 블로그 글 제목
    private String description;  // 블로그 글 설명
    private String link;         // 블로그 글 URL
    private String bloggername;  // 블로거 이름
    private String postdate;     // 게시 날짜
    private String thumbnail;    // 썸네일 URL (추가적으로 처리 가능)
}