
package com.fitable.backend.board.dto;

public class BoardSummaryResponse {
    private final String title;
    private final String loginId;
    private final Long commentCount;

    public BoardSummaryResponse(String title, String loginId, Long commentCount) {
        this.title = title;
        this.loginId = loginId;
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }

    public String getLoginId() {
        return loginId;
    }

    public Long getCommentCount() {
        return commentCount;
    }
}
