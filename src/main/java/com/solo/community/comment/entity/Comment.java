package com.solo.community.comment.entity;

import com.solo.community.basetime.BaseTimeEntity;
import com.solo.community.board.entity.Board;
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
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    public void changeBoard(Board board) {
        if(this.board != null) {
            this.board.getComments().remove(this);
        }
        this.board = board;
        if(!board.getComments().contains(this)) {
            board.addComment(this);
        }
    }
}
