package com.fitable.backend.comment.controller;

import com.fitable.backend.comment.dto.CommentRequest;
import com.fitable.backend.comment.dto.CommentResponse;
import com.fitable.backend.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponse addComment(
            @PathVariable Long boardId,
            @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.addComment(boardId, commentRequest, userDetails);
    }

    @GetMapping
    public List<CommentResponse> getCommentsByBoard(@PathVariable Long boardId) {
        return commentService.getCommentsByBoard(boardId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.updateComment(commentId, commentRequest, userDetails);
    }

}
