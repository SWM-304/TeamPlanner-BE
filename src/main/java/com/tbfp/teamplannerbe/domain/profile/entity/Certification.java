package com.tbfp.teamplannerbe.domain.profile.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CERTIFICATION_ID")
    private Long id;

    @NotNull
    private String name;

    @Nullable
    private double score;

    private LocalDate gainDate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public ProfileResponseDto.CertificationResponseDto toDto(){
        return ProfileResponseDto.CertificationResponseDto.builder()
                .id(id)
                .name(name)
                .score(score)
                .gainDate(gainDate)
                .build();
    }
}
