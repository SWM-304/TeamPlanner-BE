package com.tbfp.teamplannerbe.domain.Comment.entity;


import com.tbfp.teamplannerbe.domain.Comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMMENTID")
    private Long id;

    @Column(nullable = false)
    private String content;

    private boolean state;

//    private String memberId;

    @Column(nullable = false)
    private int depth;  // 댓글의 깊이

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(nullable = false)
    private boolean isConfidential; // true면 익명댓글 false이면 default 댓글

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        this.depth = parentComment.getDepth() + 1;  // 깊이 설정
    }

    public void setState(boolean state){
        this.state= state;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public CommentRequestDto.CommentUpdateRequestDto toDTO() {
        return CommentRequestDto.CommentUpdateRequestDto.builder()
                .commentId(id)
                .boardId(board.getId())
                .content(content)
                .build();

    }

}
