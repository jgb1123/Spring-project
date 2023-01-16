package com.solo.community.comment.controller;

import com.solo.community.comment.dto.CommentPatchDto;
import com.solo.community.comment.dto.CommentPostDto;
import com.solo.community.comment.dto.CommentResponseDto;
import com.solo.community.comment.entity.Comment;
import com.solo.community.comment.mapper.CommentMapper;
import com.solo.community.comment.service.CommentService;
import com.solo.community.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity postComment(@Positive @PathVariable Long boardId,
                                      @Valid @RequestBody CommentPostDto commentPostDto,
                                      @AuthenticationPrincipal String email) {
        Comment comment = commentMapper.commentPostDtoToComment(commentPostDto);
        Comment savedComment = commentService.createComment(email, boardId, comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity getComments(@Positive @PathVariable Long boardId,
                                      @Positive @RequestParam int page,
                                      @Positive @RequestParam int size) {
        Page<Comment> pageComments = commentService.findComments(boardId, page, size);
        List<Comment> comments = pageComments.getContent();
        List<CommentResponseDto> commentResponseDtos = commentMapper.commentsToCommentResponseDtos(comments);
        return new ResponseEntity<>(new MultiResponseDto<>(commentResponseDtos, pageComments), HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity patchComment(@Positive @PathVariable Long commentId,
                                       @Valid @RequestBody CommentPatchDto commentPatchDto,
                                       @AuthenticationPrincipal String email) {
        Comment modifiedComment = commentMapper.commentPatchDtoToComment(commentPatchDto);
        commentService.updateComment(email, commentId, modifiedComment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@Positive @PathVariable Long commentId,
                                        @AuthenticationPrincipal String email) {
        commentService.deleteComment(email, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
