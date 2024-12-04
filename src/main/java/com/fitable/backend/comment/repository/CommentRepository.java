package com.fitable.backend.comment.repository;

import com.fitable.backend.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoard_BoardId(Long boardId); // 특정 게시물의 댓글 조회
}
