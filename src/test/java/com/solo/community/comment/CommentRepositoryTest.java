package com.solo.community.comment;

import com.solo.community.comment.entity.Comment;
import com.solo.community.comment.repository.CommentRepository;
import com.solo.community.exception.BusinessLogicException;
import com.solo.community.exception.ExceptionCode;
import com.solo.community.util.CommentDummy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void saveComment() {
        //given
        Comment comment = CommentDummy.createComment1();
        //when
        Comment savedComment = commentRepository.save(comment);
        //then
        assertThat(comment.getCommentContent()).isEqualTo(savedComment.getCommentContent());
    }

    @Test
    void findComment() {
        //given
        Comment comment = CommentDummy.createComment1();
        Comment savedComment = commentRepository.save(comment);
        //when
        Comment foundComment = commentRepository.findById(savedComment.getCommentId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        //then
        assertThat(foundComment.getCommentContent()).isEqualTo(savedComment.getCommentContent());
    }

    @Test
    void findComments() {
        //given
        Comment comment1 = CommentDummy.createComment1();
        Comment comment2 = CommentDummy.createComment2();
        Comment savedComment1 = commentRepository.save(comment1);
        Comment savedComment2 = commentRepository.save(comment2);
        //when
        Page<Comment> pageComments = commentRepository.findAll(PageRequest.of(0, 10,
                Sort.by("createdAt").ascending()));
        List<Comment> comments = pageComments.getContent();
        //then
        assertThat(comments).contains(savedComment1);
        assertThat(comments).contains(savedComment2);
    }

    @Test
    void updateComment() {
        //given
        Comment comment = CommentDummy.createComment1();
        Comment modifiedComment = CommentDummy.createComment2();
        Comment savedComment = commentRepository.save(comment);
        Comment foundComment = commentRepository.findById(savedComment.getCommentId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        //when
        foundComment.changeInfo(modifiedComment);
        Comment updatedComment = commentRepository.findById(foundComment.getCommentId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        //then
        assertThat(updatedComment.getCommentContent()).isEqualTo(modifiedComment.getCommentContent());
    }

    @Test
    void deleteComment() {
        //given
        Comment comment = CommentDummy.createComment1();
        Comment savedComment = commentRepository.save(comment);
        //when
        commentRepository.delete(savedComment);
        //then
        assertThat(commentRepository.findById(savedComment.getCommentId()).isPresent()).isFalse();
    }
}
