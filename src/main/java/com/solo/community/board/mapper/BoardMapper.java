package com.solo.community.board.mapper;

import com.solo.community.board.dto.BoardPatchDto;
import com.solo.community.board.dto.BoardPostDto;
import com.solo.community.board.dto.BoardResponseDto;
import com.solo.community.board.entity.Board;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardMapper {
    public Board boardPostDtoToBoard(BoardPostDto boardPostDto) {
        return Board.builder()
                .boardTitle(boardPostDto.getBoardTitle())
                .boardContent(boardPostDto.getBoardContent())
                .build();
    }

    public Board boardPatchDtoToBoard(BoardPatchDto boardPatchDto) {
        return Board.builder()
                .boardTitle(boardPatchDto.getBoardTitle())
                .boardContent(boardPatchDto.getBoardContent())
                .build();
    }

    public BoardResponseDto boardToBoardResponseDto(Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .modifiedAt(board.getModifiedAt())
                .createdAt(board.getCreatedAt())
                .nickname(board.getMember().getNickname())
                .build();
    }

    public List<BoardResponseDto> boardsToBoardResponseDtos(List<Board> boards) {
        return boards
                .stream()
                .map(board -> boardToBoardResponseDto(board))
                .collect(Collectors.toList());
    }
}
