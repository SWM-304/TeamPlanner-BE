package com.tbfp.teamplannerbe.domain.profile.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long id;

    @NotNull
    private String subject;

    private String detail;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public ProfileResponseDto.ActivityResponseDto toDto(){
        return ProfileResponseDto.ActivityResponseDto.builder()
                .subject(subject)
                .detail(detail)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
