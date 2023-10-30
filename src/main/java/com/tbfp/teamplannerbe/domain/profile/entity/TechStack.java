package com.tbfp.teamplannerbe.domain.profile.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.profile.SkillLevel;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
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
public class TechStack extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TECH_STACK_ID")
    private Long id;

    private Integer skillLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TECH_STACK_ITEM_ID")
    private TechStackItem techStackItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public ProfileResponseDto.TechStackResponseDto toDto(){
        return ProfileResponseDto.TechStackResponseDto.builder()
                .id(id)
                .skillLevel(skillLevel)
                .techStackItem(techStackItem)
                .build();
    }
}
