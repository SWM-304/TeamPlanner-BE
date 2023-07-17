package com.tbfp.teamplannerbe.domain.recruitmentComment.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class RecruitmentComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECRUITMENT_COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECRUITMENT_ID")
    private Recruitment recruitment;

    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_COMMENT_ID")
    private RecruitmentComment parentComment;

    @Column(nullable = false)
    private boolean isConfidential;

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void makeDeleted() {
        this.isDeleted = true;
    }
}
