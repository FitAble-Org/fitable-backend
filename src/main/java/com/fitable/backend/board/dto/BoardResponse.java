package com.fitable.backend.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private String loginId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
