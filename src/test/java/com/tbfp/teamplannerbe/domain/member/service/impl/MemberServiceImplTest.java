package com.tbfp.teamplannerbe.domain.member.service.impl;

import com.tbfp.teamplannerbe.common.BaseControllerTest;
import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberJpaRepository;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceImplTest extends BaseControllerTest {

    @Autowired
    EntityManager em;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    MemberService memberService;





    @Test
    @Transactional
    public void 회원가입테스트() throws Exception{

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                        .loginId("test1")
                        .password("1234")
                        .email("swkwon25@gmail.com")
                        .phone("010-3170-8048")
                        .profileIntro("나는 훌륭한 일꾼입니다")
                        .profileImage("dd.image")
                        .job(Job.valueOf("COLLEGE"))
                        .education(Education.valueOf("COLLEGE"))
                        .educationGrade(4)
                        .birth(LocalDate.parse("2010-10-11"))
                        .address("경기도 용인시 처인구 우리집")
                        .gender(Gender.valueOf("MALE"))
                        .kakaoId("kakaoId")
                        .contactEmail("swkwon25@gmail.com")
                        .isPublic(Long.valueOf("1023"))
                        .loginIdChecked(true)
                        .emailChecked(true)
                    .build());


        List<Member> member = memberJpaRepository.findAll();

        Assertions.assertThat(member.stream().count()).isEqualTo(1);



    }



   
}