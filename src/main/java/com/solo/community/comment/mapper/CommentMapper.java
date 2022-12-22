package com.solo.community.comment.mapper;

import com.solo.community.comment.dto.CommentPatchDto;
import com.solo.community.comment.dto.CommentPostDto;
import com.solo.community.comment.dto.CommentResponseDto;
import com.solo.community.comment.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    public Comment commentPostDtoToComment(CommentPostDto commentPostDto) {
        return Comment.builder()
                .commentContent(commentPostDto.getCommentContent())
                .build();
    }

    public Comment commentPatchDtoToComment(CommentPatchDto commentPatchDto) {
        return Comment.builder()
                .commentContent(commentPatchDto.getCommentContent())
                .build();
    }

    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .commentContent(comment.getCommentContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .nickname(comment.getMember().getNickname())
                .build();
    }

    public List<CommentResponseDto> commentsToCommentResponseDtos(List<Comment> comments) {
        return comments
                .stream()
                .map(comment -> commentToCommentResponseDto(comment))
                .collect(Collectors.toList());
    }
}
