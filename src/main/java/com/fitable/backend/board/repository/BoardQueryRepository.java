package com.fitable.backend.board.repository;

import com.fitable.backend.board.entity.QBoard;
import com.fitable.backend.comment.entity.QComment;
import com.fitable.backend.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.fitable.backend.board.dto.BoardSummaryResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QBoard board = QBoard.board;
    private final QComment comment = QComment.comment;
    private final QUser user = QUser.user;

    public BoardQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<BoardSummaryResponse> findBoardSummaries() {
        return queryFactory
                .select(Projections.constructor(
                        BoardSummaryResponse.class,
                        board.title,
                        user.loginId,
                        comment.count().coalesce(0L)
                ))
                .from(board)
                .join(board.user, user)
                .leftJoin(comment).on(comment.board.eq(board))
                .groupBy(board.title, user.loginId)
                .fetch();
    }
}
