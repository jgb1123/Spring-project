package com.solo.community.board.controller;

import com.solo.community.board.dto.BoardPatchDto;
import com.solo.community.board.dto.BoardPostDto;
import com.solo.community.board.dto.BoardResponseDto;
import com.solo.community.board.entity.Board;
import com.solo.community.board.mapper.BoardMapper;
import com.solo.community.board.service.BoardService;
import com.solo.community.dto.MultiResponseDto;
import com.solo.community.dto.SingleResponseDto;
import com.solo.community.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {
    private final BoardMapper boardMapper;
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity postBoard(@RequestBody BoardPostDto boardPostDto,
                                    @AuthenticationPrincipal String email) {
        Board board = boardMapper.boardPostDtoToBoard(boardPostDto);
        Board savedBoard = boardService.createBoard(email, board);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @GetMapping("/{boardId}")
    public ResponseEntity getBoard(@PathVariable Long boardId) {
        Board foundBoard = boardService.findBoard(boardId);
        BoardResponseDto boardResponseDto = boardMapper.boardToBoardResponseDto(foundBoard);
        return new ResponseEntity<>(new SingleResponseDto<>(boardResponseDto), HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity getBoards(@RequestParam int page,
                                    @RequestParam int size) {
        Page<Board> pageBoards = boardService.findBoards(page, size);
        List<Board> boards = pageBoards.getContent();
        List<BoardResponseDto> boardResponseDtos = boardMapper.boardsToBoardResponseDtos(boards);
        return new ResponseEntity<>(new MultiResponseDto<>(boardResponseDtos, pageBoards), HttpStatus.OK);
    }
    
    @PatchMapping("/{boardId}")
    public ResponseEntity patchBoard(@PathVariable Long boardId,
                                     @RequestBody BoardPatchDto boardPatchDto,
                                     @AuthenticationPrincipal String email) {
        Board modifiedBoard = boardMapper.boardPatchDtoToBoard(boardPatchDto);
        boardService.updateBoard(boardId, modifiedBoard, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId,
                                      @AuthenticationPrincipal String email) {
        boardService.deleteBoard(boardId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
