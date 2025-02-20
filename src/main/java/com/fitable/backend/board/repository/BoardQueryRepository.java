package com.fitable.backend.board.repository;

import com.fitable.backend.board.entity.QBoard;
import com.fitable.backend.comment.entity.QComment;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardQueryRepository {

    private final JPAQueryFactory queryFactory;

    public BoardQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Tuple> findAllBoardsWithCommentCount() {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        return queryFactory
                .select(board, comment.count())
                .from(board)
                .leftJoin(comment).on(comment.board.eq(board))
                .groupBy(board)
                .fetch();
    }
}
