package com.fitable.backend.board.controller;

import com.fitable.backend.board.dto.BoardRequest;
import com.fitable.backend.board.dto.BoardResponse;
import com.fitable.backend.board.dto.BoardSummaryResponse;
import com.fitable.backend.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public BoardResponse createBoard(
            @RequestBody BoardRequest boardRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        return boardService.createBoard(boardRequest, userDetails);
    }

    @GetMapping("/{boardId}")
    public BoardResponse getBoard(@PathVariable Long boardId) {
        return boardService.getBoardById(boardId);
    }

    @GetMapping
    public List<BoardSummaryResponse> getAllBoards() {
        return boardService.getBoardSummaries();
    }

    @PutMapping("/{boardId}")
    public BoardResponse updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequest boardRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        return boardService.updateBoard(boardId, boardRequest, userDetails);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetails userDetails) {
        boardService.deleteBoard(boardId, userDetails);
    }
}
