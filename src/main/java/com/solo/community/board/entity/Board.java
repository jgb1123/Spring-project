package com.solo.community.board.entity;

import com.solo.community.basetime.BaseTimeEntity;
import com.solo.community.comment.entity.Comment;
import com.solo.community.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void changeMember(Member member) {
        this.member = member;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        if(comment.getBoard() != this) {
            comment.changeBoard(this);
        }
    }

    public void changeInfo(Board board) {
        if(board.getBoardTitle() != null) this.boardTitle = board.getBoardTitle();
        if(board.getBoardContent() != null) this.boardContent = board.getBoardContent();
    }

    public void increaseView() {
        this.view++;
    }
}
