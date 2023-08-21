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
import java.time.YearMonth;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BASIC_PROFILE_ID")
    private Long id;

    @Column(length = 1000)
    private String profileIntro;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Job job;

    @Enumerated(EnumType.STRING)
    private Education education;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private LocalDate birth;

    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String kakaoId;

    private String contactEmail;

    private Long isPublic;

    @Column(columnDefinition = "bit default 0")
    private Boolean evaluationPublic;

    @OneToOne(mappedBy = "basicProfile", cascade = CascadeType.ALL)
    private Member member;

    public ProfileResponseDto.BasicProfileResponseDto toDto(){
        return ProfileResponseDto.BasicProfileResponseDto.builder()
                .id(id)
                .profileIntro(profileIntro)
                .profileImage(profileImage)
                .job(job)
                .education(education)
                .admissionDate(admissionDate)
                .graduationDate(graduationDate)
                .birth(birth)
                .address(address)
                .gender(gender)
                .kakaoId(kakaoId)
                .contactEmail(contactEmail)
                .isPublic(isPublic)
                .evaluationPublic(evaluationPublic)
                .build();
    }

//    public Member addBasicProfiletoMember(Member member) {
//        System.out.println();
//        // Member 관계 설정
//        this.member = member;
//        this.member.addBasicProfile(this); // 이 부분이 Member 엔티티와의 관계 설정을 위한 부분입니다.
//        return this.member;
//    }
}
