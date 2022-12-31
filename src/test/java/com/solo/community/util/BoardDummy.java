package com.solo.community.util;

import com.solo.community.board.dto.BoardPatchDto;
import com.solo.community.board.dto.BoardPostDto;
import com.solo.community.board.dto.BoardResponseDto;
import com.solo.community.board.entity.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface BoardDummy {
    static Board createBoard1() {
        return Board.builder()
                .boardId(1L)
                .boardTitle("글제목1")
                .boardContent("글내용1")
                .view(0)
                .member(null)
                .comments(new ArrayList<>())
                .build();
    }

    static Board createBoard2() {
        return Board.builder()
                .boardId(2L)
                .boardTitle("글제목2")
                .boardContent("글내용2")
                .view(0)
                .member(null)
                .comments(new ArrayList<>())
                .build();
    }

    static BoardPostDto createPostDto() {
        return BoardPostDto.builder()
                .boardTitle("글제목1")
                .boardContent("글내용1")
                .build();
    }

    static BoardPatchDto createPatchDto() {
        return BoardPatchDto.builder()
                .boardTitle("변경된 글제목")
                .boardContent("변경된 글내용")
                .build();
    }

    static BoardResponseDto createdResponseDto1() {
        return BoardResponseDto.builder()
                .boardId(1L)
                .boardTitle("글제목1")
                .boardContent("글내용1")
                .view(0)
                .createdAt(LocalDateTime.of(2022,12,18,0,0,0))
                .modifiedAt(LocalDateTime.of(2022,12,18,0,0,0))
                .nickname("hgd123")
                .build();
    }

    static BoardResponseDto createdResponseDto2() {
        return BoardResponseDto.builder()
                .boardId(2L)
                .boardTitle("글제목2")
                .boardContent("글내용2")
                .view(0)
                .createdAt(LocalDateTime.of(2022,12,18,0,0,0))
                .modifiedAt(LocalDateTime.of(2022,12,18,0,0,0))
                .nickname("lss123")
                .build();
    }
}
