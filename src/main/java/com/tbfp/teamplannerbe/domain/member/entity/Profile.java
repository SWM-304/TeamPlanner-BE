package com.tbfp.teamplannerbe.domain.member.entity;

import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
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

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

}
