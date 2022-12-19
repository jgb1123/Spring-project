package com.solo.community.board;

import com.solo.community.board.entity.Board;
import com.solo.community.board.repository.BoardRepository;
import com.solo.community.exception.BusinessLogicException;
import com.solo.community.exception.ExceptionCode;
import com.solo.community.member.repository.MemberRepository;
import com.solo.community.util.BoardDummy;
import com.solo.community.util.MemberDummy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    void saveBoard() {
        //given
        Board board = BoardDummy.createBoard1();
        //when
        Board savedBoard = boardRepository.save(board);
        //then
        assertThat(board.getBoardContent()).isEqualTo(savedBoard.getBoardContent());
    }

    @Test
    void findBoard() {
        //given
        Board board1 = BoardDummy.createBoard1();
        Board savedBoard1 = boardRepository.save(board1);
        //when
        Board foundBoard1 = boardRepository.findById(savedBoard1.getBoardId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        //then
        assertThat(foundBoard1.getBoardTitle()).isEqualTo(savedBoard1.getBoardTitle());
        assertThat(foundBoard1.getBoardContent()).isEqualTo(savedBoard1.getBoardContent());
    }

    @Test
    void findBoards() {
        //given
        Board board1 = BoardDummy.createBoard1();
        Board board2 = BoardDummy.createBoard2();
        Board savedBoard1 = boardRepository.save(board1);
        Board savedBoard2 = boardRepository.save(board2);
        //when
        Page<Board> pageBoards = boardRepository.findAll(PageRequest.of(0, 10, Sort.by("boardId").ascending()));
        List<Board> content = pageBoards.getContent();
        //then
        assertThat(content).contains(savedBoard1);
        assertThat(content).contains(savedBoard2);
    }

    @Test
    void updateBoard() {
        //given
        Board board = BoardDummy.createBoard1();
        Board modifiedBoard = BoardDummy.createBoard2();
        Board savedBoard = boardRepository.save(board);
        Board foundBoard = boardRepository.findById(savedBoard.getBoardId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        //when
        foundBoard.changeInfo(modifiedBoard);
        Board updatedBoard = boardRepository.findById(foundBoard.getBoardId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        //then
        assertThat(updatedBoard.getBoardTitle()).isEqualTo(modifiedBoard.getBoardTitle());
    }

    @Test
    void deleteBoard() {
        //given
        Board board = BoardDummy.createBoard1();
        Board savedBoard = boardRepository.save(board);
        //when
        boardRepository.deleteById(savedBoard.getBoardId());
        //then
        assertThat(boardRepository.findById(savedBoard.getBoardId()).isPresent()).isFalse();
    }
}
