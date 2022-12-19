package com.solo.community.board;

import com.solo.community.board.entity.Board;
import com.solo.community.board.repository.BoardRepository;
import com.solo.community.board.service.BoardService;
import com.solo.community.exception.BusinessLogicException;
import com.solo.community.member.entity.Member;
import com.solo.community.member.service.MemberService;
import com.solo.community.util.BoardDummy;
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
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberService memberService;

    @Test
    void createBoardTest() {
        //given
        Board board = BoardDummy.createBoard1();
        Member member = MemberDummy.createMember1();
        board.changeMember(member);
        given(boardRepository.save(Mockito.any(Board.class)))
                .willReturn(board);
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        //when
        Board savedBoard = boardService.createBoard("hgd@gmail.com", board);
        //then
        assertThat(board.getBoardContent()).isEqualTo(savedBoard.getBoardContent());
    }

    @Test
    void findBoardTest(){
        //given
        Board board1 = BoardDummy.createBoard1();
        given(boardRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(board1));
        //when
        Board foundBoard = boardService.findBoard(1L);
        //then
        assertThat(board1.getBoardTitle()).isEqualTo(foundBoard.getBoardTitle());
        assertThat(board1.getBoardContent()).isEqualTo(foundBoard.getBoardContent());
    }

    @Test
    void findBoardsTest() {
        //given
        Board board1 = BoardDummy.createBoard1();
        Board board2 = BoardDummy.createBoard2();
        given(boardRepository.findAll(PageRequest.of(0, 10, Sort.by("boardId").ascending())))
                .willReturn(new PageImpl<>(List.of(board1, board2), PageRequest.of(0, 10, Sort.by("boardId").ascending()), 2));
        Page<Board> pageBoards = boardService.findBoards(1, 10);
        //then
        assertThat(pageBoards.getContent()).contains(board1);
        assertThat(pageBoards.getContent()).contains(board2);
    }

    @Test
    void updateBoardTest() {
        //given
        Board board = BoardDummy.createBoard1();
        Member member = MemberDummy.createMember1();
        board.changeMember(member);
        Board modifiedBoard = BoardDummy.createBoard2();
        given(boardRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(board));
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        //when
        Board updatedBoard = boardService.updateBoard(1L, modifiedBoard, "hgd@gmail.com");
        //then
        assertThat(modifiedBoard.getBoardTitle()).isEqualTo(updatedBoard.getBoardTitle());
        assertThat(modifiedBoard.getBoardContent()).isEqualTo(updatedBoard.getBoardContent());
    }

    @Test
    void deleteBoardTest() {
        //given
        Board board = BoardDummy.createBoard1();
        Member member = MemberDummy.createMember1();
        board.changeMember(member);
        given(boardRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(board));
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        //then
        Assertions.assertThrows(BusinessLogicException.class, () -> boardService.deleteBoard(board.getBoardId(), "abcd@gmail.com"));

    }
}
