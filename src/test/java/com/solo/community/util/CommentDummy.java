package com.solo.community.util;

import com.solo.community.comment.dto.CommentPatchDto;
import com.solo.community.comment.dto.CommentPostDto;
import com.solo.community.comment.dto.CommentResponseDto;
import com.solo.community.comment.entity.Comment;

import java.time.LocalDateTime;

public interface CommentDummy {
    static Comment createComment1() {
        return Comment.builder()
                .commentId(1L)
                .commentContent("댓글내용1")
                .build();
    }

    static Comment createComment2() {
        return Comment.builder()
                .commentId(2L)
                .commentContent("댓글내용2")
                .build();
    }

    static CommentPostDto createPostDto() {
        return CommentPostDto.builder()
                .commentContent("댓글내용1")
                .build();
    }

    static CommentPatchDto createPatchDto() {
        return CommentPatchDto.builder()
                .commentContent("변경된 댓글내용")
                .build();
    }

    static CommentResponseDto createResponseDto1() {
        return CommentResponseDto.builder()
                .commentId(1L)
                .commentContent("댓글내용1")
                .createdAt(LocalDateTime.of(2022, 12, 23, 0, 0, 0))
                .modifiedAt(LocalDateTime.of(2022, 12, 23, 0, 0, 0))
                .nickname("hgd123")
                .build();
    }

    static CommentResponseDto createResponseDto2() {
        return CommentResponseDto.builder()
                .commentId(2L)
                .commentContent("댓글내용2")
                .createdAt(LocalDateTime.of(2022, 12, 23, 0, 0, 0))
                .modifiedAt(LocalDateTime.of(2022, 12, 23, 0, 0, 0))
                .nickname("lss123")
                .build();
    }


}
