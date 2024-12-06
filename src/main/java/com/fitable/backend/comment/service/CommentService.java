package com.fitable.backend.comment.service;

import com.fitable.backend.board.entity.Board;
import com.fitable.backend.board.repository.BoardRepository;
import com.fitable.backend.comment.dto.CommentRequest;
import com.fitable.backend.comment.dto.CommentResponse;
import com.fitable.backend.comment.entity.Comment;
import com.fitable.backend.comment.repository.CommentRepository;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse addComment(Long boardId, CommentRequest commentRequest, UserDetails userDetails) {
        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + userDetails.getUsername()));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(user);
        comment.setContent(commentRequest.getContent());

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByBoard(Long boardId) {
        List<Comment> comments = commentRepository.findByBoard_BoardId(boardId);
        return comments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, UserDetails userDetails) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));

        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + userDetails.getUsername()));

        if (!comment.getUser().equals(user)) {
            throw new SecurityException("User not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setContent(comment.getContent());
        response.setLoginId(comment.getUser().getLoginId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }
}
