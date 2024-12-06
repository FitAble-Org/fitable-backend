package com.fitable.backend.board.repository;

import com.fitable.backend.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b, COUNT(c) as commentCount " +
            "FROM Board b " +
            "LEFT JOIN Comment c ON c.board = b " +
            "GROUP BY b")
    List<Object[]> findAllBoardsWithCommentCount();
}
