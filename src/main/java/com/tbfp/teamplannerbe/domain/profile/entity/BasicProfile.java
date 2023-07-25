package com.tbfp.teamplannerbe.domain.profile.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILE_ID")
    private Long id;

    @Column(length = 1000)
    private String profileIntro;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Job job;

    @Enumerated(EnumType.STRING)
    private Education education;

    private int educationGrade;

    private LocalDate birth;

    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String kakaoId;

    private String contactEmail;

    private Long isPublic;

    @Column(columnDefinition = "bit default 0")
    private Boolean evaluationPublic;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public ProfileResponseDto.BasicProfileResponseDto toDto(){
        return ProfileResponseDto.BasicProfileResponseDto.builder()
                .profileIntro(profileIntro)
                .profileImage(profileImage)
                .job(job)
                .education(education)
                .educationGrade(educationGrade)
                .birth(birth)
                .address(address)
                .gender(gender)
                .kakaoId(kakaoId)
                .contactEmail(contactEmail)
                .isPublic(isPublic)
                .evaluationPublic(evaluationPublic)
                .build();
    }

}
