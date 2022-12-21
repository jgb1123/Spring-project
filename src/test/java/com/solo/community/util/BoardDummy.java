package com.solo.community.util;

import com.solo.community.board.dto.BoardPatchDto;
import com.solo.community.board.dto.BoardPostDto;
import com.solo.community.board.dto.BoardResponseDto;
import com.solo.community.board.entity.Board;
import com.solo.community.member.dto.MemberPatchDto;
import com.solo.community.member.dto.MemberPostDto;
import com.solo.community.member.dto.MemberResponseDto;
import com.solo.community.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BoardDummy {
    static Board createBoard1() {
        return new Board(1L, "글제목1", "글내용1", 0, null, new ArrayList<>());
    }

    static Board createBoard2() {
        return new Board(2L, "글제목2", "글내용2", 0, null, new ArrayList<>());
    }

    static BoardPostDto createPostDto() {
        return new BoardPostDto("글제목1", "글내용1");
    }

    static BoardPatchDto createPatchDto() {
        return new BoardPatchDto("변경된 글제목", "변경된 글내용");
    }

    static BoardResponseDto createdResponseDto1() {
        return new BoardResponseDto(1L, "글제목1", "글내용1", 0, LocalDateTime.of(2022,12,18,0,0,0), LocalDateTime.of(2022,12,18,0,0,0), "hgd123");
    }

    static BoardResponseDto createdResponseDto2() {
        return new BoardResponseDto(2L, "글제목2", "글내용2", 0, LocalDateTime.of(2022,12,18,0,0,0), LocalDateTime.of(2022,12,18,0,0,0), "lss123");
    }


}
