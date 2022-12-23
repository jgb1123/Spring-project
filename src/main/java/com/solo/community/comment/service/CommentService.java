package com.solo.community.comment.service;

import com.solo.community.board.entity.Board;
import com.solo.community.board.service.BoardService;
import com.solo.community.comment.entity.Comment;
import com.solo.community.comment.repository.CommentRepository;
import com.solo.community.exception.BusinessLogicException;
import com.solo.community.exception.ExceptionCode;
import com.solo.community.member.entity.Member;
import com.solo.community.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final BoardService boardService;

    public Comment createComment(String email, Long boardId, Comment comment) {
        Member foundMember = memberService.findVerifiedMember(email);
        Board foundBoard = boardService.findVerifiedBoard(boardId);
        comment.changeMember(foundMember);
        comment.changeBoard(foundBoard);
        return commentRepository.save(comment);
    }

    public Page<Comment> findComments(Long boardId, int page, int size) {
        Board foundBoard = boardService.findVerifiedBoard(boardId);

        return commentRepository.findAllByBoard(foundBoard, PageRequest.of(page - 1, size,
                Sort.by("createdAt").ascending()));
    }

    public Comment updateComment(String email, Long commentId, Comment modifiedComment) {
        Comment foundComment = findVerifiedComment(commentId);
        emailConfirm(email, foundComment);
        foundComment.changeInfo(modifiedComment);
        return foundComment;
    }

    public void deleteComment(String email, Long commentId) {
        Comment foundComment = findVerifiedComment(commentId);
        emailConfirm(email, foundComment);
        commentRepository.delete(foundComment);
    }

    public Comment findVerifiedComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

    private void emailConfirm(String email, Comment foundComment) {
        if(!foundComment.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_COMMENT);
        }
    }
}
