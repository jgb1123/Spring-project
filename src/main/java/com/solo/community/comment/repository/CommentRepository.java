package com.solo.community.comment.repository;

import com.solo.community.board.entity.Board;
import com.solo.community.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByBoard(Board board, Pageable pageable);
}
