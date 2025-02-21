package com.fitable.backend.board.service;

import com.fitable.backend.board.dto.BoardRequest;
import com.fitable.backend.board.dto.BoardResponse;
import com.fitable.backend.board.dto.BoardSummaryResponse;
import com.fitable.backend.board.entity.Board;
import com.fitable.backend.board.entity.QBoard;
import com.fitable.backend.board.repository.BoardQueryRepository;
import com.fitable.backend.board.repository.BoardRepository;
import com.fitable.backend.comment.entity.QComment;
import com.fitable.backend.user.entity.User;
import com.fitable.backend.user.repository.UserRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardQueryRepository boardQueryRepository;

    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest, UserDetails userDetails) {
        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + userDetails.getUsername()));

        Board board = new Board();
        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContent());
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);
        return mapToResponseDto(savedBoard);
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        return mapToResponseDto(board);
    }

    @Transactional(readOnly = true)
    public List<BoardSummaryResponse> getBoardSummaries() {
        return boardQueryRepository.findBoardSummaries();
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest, UserDetails userDetails) {
        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + userDetails.getUsername()));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        if (!board.getUser().equals(user)) {
            throw new SecurityException("User not authorized to update this board");
        }

        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContent());
        Board updatedBoard = boardRepository.save(board);

        return mapToResponseDto(updatedBoard);
    }

    @Transactional
    public void deleteBoard(Long boardId, UserDetails userDetails) {
        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + userDetails.getUsername()));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        if (!board.getUser().equals(user)) {
            throw new SecurityException("User not authorized to delete this board");
        }

        boardRepository.delete(board);
    }

    private BoardResponse mapToResponseDto(Board board) {
        return mapToResponseDto(board, 0L); // 기본 댓글 개수 0으로 설정
    }

    private BoardResponse mapToResponseDto(Board board, Long commentCount) {
        BoardResponse responseDto = new BoardResponse();
        responseDto.setBoardId(board.getBoardId());
        responseDto.setTitle(board.getTitle());
        responseDto.setContent(board.getContent());
        responseDto.setLoginId(board.getUser().getLoginId());
        responseDto.setCreatedAt(board.getCreatedAt());
        responseDto.setUpdatedAt(board.getUpdatedAt());
        responseDto.setCommentCount(commentCount);
        return responseDto;
    }
}
