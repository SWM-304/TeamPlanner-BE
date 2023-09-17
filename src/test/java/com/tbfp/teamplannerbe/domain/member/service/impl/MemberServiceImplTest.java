package com.tbfp.teamplannerbe.domain.member.service.impl;

import com.tbfp.teamplannerbe.common.BaseControllerTest;
import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

class MemberServiceImplTest extends BaseControllerTest {

    @Autowired
    EntityManager em;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;





    @Test
    @Transactional
    public void 회원가입테스트() throws Exception{

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                        .username("test1")
                        .password("1234")
                        .email("asdas@gmail.com")
                        .profileIntro("나는 훌륭한 일꾼입니다")
                        .profileImage("dd.image")
                        .job(Job.valueOf("COLLEGE"))
                        .education(Education.valueOf("COLLEGE"))
                        .admissionDate(LocalDate.parse("2017-03-01"))
                        .graduationDate(LocalDate.parse(("2022-02-01")))
                        .birth(LocalDate.parse("2010-10-11"))
                        .address("경기도 시흥 아몰라 우리집")
                        .gender(Gender.valueOf("MALE"))
                        .kakaoId("kakaoId")
                        .contactEmail("asdas@gmail.com")
                        .isPublic(Long.valueOf("1023"))
                    .build());


        List<Member> member = memberRepository.findAll();

        Assertions.assertThat(member.stream().count()).isEqualTo(1);
    }
}