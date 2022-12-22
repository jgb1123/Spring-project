package com.solo.community.board.service;

import com.solo.community.board.entity.Board;
import com.solo.community.board.repository.BoardRepository;
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
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    public Board createBoard(String email, Board board) {
        Member foundMember = memberService.findVerifiedMember(email);
        board.changeMember(foundMember);
        return boardRepository.save(board);
    }

    public Board findBoard(Long boardId) {
        return findVerifiedBoard(boardId);
    }

    public Page<Board> findBoards(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page - 1, size, Sort.by("boardId").ascending()));
    }

    public Board updateBoard(Long boardId, Board modifiedBoard, String email) {
        memberService.findVerifiedMember(email);
        Board foundBoard = findVerifiedBoard(boardId);
        emailConfirm(email, foundBoard);
        foundBoard.changeInfo(modifiedBoard);
        return foundBoard;
    }

    public void deleteBoard(Long boardId, String email) {
        memberService.findVerifiedMember(email);
        Board foundBoard = findVerifiedBoard(boardId);
        emailConfirm(email, foundBoard);
        boardRepository.delete(foundBoard);
    }

    public Board findVerifiedBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
    }

    private void emailConfirm(String email, Board foundBoard) {
        if(!foundBoard.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_BOARD);
        }
    }
}
