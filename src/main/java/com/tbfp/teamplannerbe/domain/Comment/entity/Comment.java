package com.tbfp.teamplannerbe.domain.comment.entity;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    private boolean state;

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

    public void changeState(boolean state){
        this.state= state;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    @Builder
    public Comment(Long id, String content, boolean state, int depth, Board board, Member member, Comment parentComment, boolean isConfidential) {
        this.id = id;
        this.content = content;
        this.state = state;
        this.depth = depth;
        this.board = board;
        this.member = member;
        this.parentComment = parentComment;
        this.isConfidential = isConfidential;
    }

    public CommentRequestDto.CommentUpdateRequestDto toDTO() {
        return CommentRequestDto.CommentUpdateRequestDto.builder()
                .commentId(id)
                .boardId(board.getId())
                .content(content)
                .build();

    }

}
