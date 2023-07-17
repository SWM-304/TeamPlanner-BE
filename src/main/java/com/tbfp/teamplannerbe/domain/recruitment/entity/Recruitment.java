package com.tbfp.teamplannerbe.domain.recruitment.entity;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.RecruitmentUpdateRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Recruitment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECRUITMENT_ID")
    private Long id;

    private String title;
    private String content;

    @Builder.Default
    private Integer maxMemberSize = 0;
    @Builder.Default
    private Integer currentMemberSize = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member author;

    @Builder.Default
    private Integer viewCount = 0;
    @Builder.Default
    private Integer likeCount = 0;

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void update(RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        this.title = recruitmentUpdateRequestDto.getNewTitle();
        this.content = recruitmentUpdateRequestDto.getNewContent();
        this.currentMemberSize = recruitmentUpdateRequestDto.getNewCurrentMemberSize();
        this.maxMemberSize = recruitmentUpdateRequestDto.getNewMaxMemberSize();
    }
}
