package com.fitable.backend.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long commentId;
    private String content;
    private String loginId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
