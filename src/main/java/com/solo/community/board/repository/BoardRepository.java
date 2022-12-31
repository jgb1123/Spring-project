package com.solo.community.board.repository;

import com.solo.community.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @EntityGraph(attributePaths = "member")
    Page<Board> findAll(Pageable pageable);
}
