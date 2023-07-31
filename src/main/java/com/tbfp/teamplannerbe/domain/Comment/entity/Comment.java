package com.tbfp.teamplannerbe.domain.comment.entity;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
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

    @Builder.Default
    private Integer childCommentCount = 0;




    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        this.depth = parentComment.getDepth() + 1;  // 깊이 설정
    }

    public void incrementchildCommentCount() {
        this.childCommentCount++;
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

    public CommentResponseDto.CreatedCommentResponseDto toDto() {
        return CommentResponseDto.CreatedCommentResponseDto.builder()
                .content(content)
                .boardId(board.getId())
                .username(member.getUsername())
                .createdDate(LocalDateTime.now())
                .isConfidential(isConfidential)
                .commentId(id)
                .build();

    }

}
