package com.solo.community.comment;

import com.solo.community.board.entity.Board;
import com.solo.community.board.service.BoardService;
import com.solo.community.comment.entity.Comment;
import com.solo.community.comment.repository.CommentRepository;
import com.solo.community.comment.service.CommentService;
import com.solo.community.exception.BusinessLogicException;
import com.solo.community.member.entity.Member;
import com.solo.community.member.service.MemberService;
import com.solo.community.util.BoardDummy;
import com.solo.community.util.CommentDummy;
import com.solo.community.util.MemberDummy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private BoardService boardService;

    @Test
    void createCommentTest() {
        //given
        Comment comment = CommentDummy.createComment1();
        Member member = MemberDummy.createMember1();
        Board board = BoardDummy.createBoard1();

        given(commentRepository.save(Mockito.any(Comment.class)))
                .willReturn(comment);
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(boardService.findVerifiedBoard(Mockito.anyLong()))
                .willReturn(board);
        //when
        Comment savedComment = commentService.createComment("hgd@gmail.com", 1L, comment);
        //then
        assertThat(comment.getCommentContent()).isEqualTo(savedComment.getCommentContent());
    }

    @Test
    void findCommentsTest() {
        //given
        Comment comment1 = CommentDummy.createComment1();
        Comment comment2 = CommentDummy.createComment2();
        Board board = BoardDummy.createBoard1();
        comment1.changeBoard(board);
        comment2.changeBoard(board);
        given(boardService.findVerifiedBoard(Mockito.anyLong()))
                .willReturn(board);
        given(commentRepository.findAllByBoard(board, PageRequest.of(0, 10, Sort.by("createdAt").ascending())))
                .willReturn(new PageImpl<>(List.of(comment1, comment2), PageRequest.of(0, 10, Sort.by("createdAt").ascending()), 2));
        //when
        Page<Comment> pageComments = commentService.findComments(1L, 1, 10);
        List<Comment> comments = pageComments.getContent();
        //then
        assertThat(comments).contains(comment1);
        assertThat(comments).contains(comment2);
    }

    @Test
    void updateCommentTest() {
        //given
        Comment comment = CommentDummy.createComment1();
        Comment modifiedComment = CommentDummy.createComment2();
        Member member = MemberDummy.createMember1();
        Board board = BoardDummy.createBoard1();
        comment.changeBoard(board);
        comment.changeMember(member);
        given(commentRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(comment));
        //when
        Comment updatedComment = commentService.updateComment("hgd@gmail.com", 1L, modifiedComment);
        //then
        assertThat(updatedComment.getCommentContent()).isEqualTo(modifiedComment.getCommentContent());
    }

    @Test
    void deleteCommentTest() {
        //given
        Comment comment = CommentDummy.createComment1();
        Member member = MemberDummy.createMember1();
        Board board = BoardDummy.createBoard1();
        comment.changeMember(member);
        comment.changeBoard(board);
        given(commentRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(comment));
        //then
        Assertions.assertThrows(BusinessLogicException.class, () -> commentService.deleteComment("abcd@gmail.com", comment.getCommentId()));
    }
}
