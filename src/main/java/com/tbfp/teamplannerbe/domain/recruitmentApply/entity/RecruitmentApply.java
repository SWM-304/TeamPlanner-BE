package com.tbfp.teamplannerbe.domain.recruitmentApply.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecruitmentApply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECRUITMENT_APPLY_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecruitmentApplyStateEnum state;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECRUITMENT_ID")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member applicant;
}
