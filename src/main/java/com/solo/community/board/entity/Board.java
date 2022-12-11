package com.solo.community.board.entity;

import com.solo.community.basetime.BaseTimeEntity;
import com.solo.community.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardContent;

    @Column
    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeInfo(Board board) {
        if(board.getBoardTitle() != null) this.boardTitle = board.getBoardTitle();
        if(board.getBoardContent() != null) this.boardContent = board.getBoardContent();
    }

}
