package com.tbfp.teamplannerbe.domain.team.service.impl;

import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.RecruitmentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto.RecruitmentCreateResponseDto;
import com.tbfp.teamplannerbe.domain.recruitment.service.RecruitmentService;
import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyRequestDto.CreateApplyRequest;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApply;
import com.tbfp.teamplannerbe.domain.recruitmentApply.repository.RecruitmentApplyRepository;
import com.tbfp.teamplannerbe.domain.recruitmentApply.service.RecruitmentApplyService;
import com.tbfp.teamplannerbe.domain.team.dto.TeamReqeustDto.CreatTeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto.createdTeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.MemberTeamRepository;
import com.tbfp.teamplannerbe.domain.team.repository.TeamRepository;
import com.tbfp.teamplannerbe.domain.team.service.TeamService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@Transactional
class TeamServiceImplTest {

     @Autowired
     MemberService memberService;
     @Autowired
     TeamRepository teamRepository;
     @Autowired
     MemberRepository memberRepository
             ;
     @Autowired
     MemberTeamRepository memberTeamRepository;
     @Autowired
     RecruitmentApplyRepository recruitmentApplyRepository;
     @Autowired
    RecruitmentApplyService recruitmentApplyService;
     @Autowired
     BoardService boardService;
     @Autowired
    RecruitmentService recruitmentService;
    @Autowired
    TeamService teamService;


    @Test
    public void 모집글_팀원신청(){
        //given
        셋업멤버();
        Long boardId = 공모전생성();

        RecruitmentCreateRequestDto recruitment = RecruitmentCreateRequestDto.builder()
                .title("모집글 제목입니다.")
                .content("저랑 같이 팀하실분 구합니다")
                .currentMemberSize(0)
                .maxMemberSize(5)
                .boardId(boardId)
                .build();

        CreateApplyRequest applyDto = CreateApplyRequest.builder()
                .content("저 팀에 참여하고싶습니다 받아주세요 ")
                .build();

        // when

        RecruitmentCreateResponseDto result = recruitmentService.createRecruitment("test0", recruitment);

        List<Member> member = memberRepository.findAll();

        //then
        for (Member member1 : member) {
            if(!member1.getUsername().equals("test0")){
                recruitmentApplyService.apply(member1.getUsername(), result.getId(), applyDto);
            }
        }

        List<RecruitmentApply> findApply = recruitmentApplyRepository.findAll();

        Assertions.assertThat(findApply.stream().count()).isEqualTo(member.stream().count()-1);




    }

    @Test
    public void 팀생성(){
        //given
        셋업멤버();
        Long boardId = 공모전생성();

        RecruitmentCreateRequestDto recruitment = RecruitmentCreateRequestDto.builder()
                .title("모집글 제목입니다.")
                .content("저랑 같이 팀하실분 구합니다")
                .currentMemberSize(0)
                .maxMemberSize(5)
                .boardId(boardId)
                .build();

        CreateApplyRequest applyDto = CreateApplyRequest.builder()
                .content("저 팀에 참여하고싶습니다 받아주세요 ")
                .build();
        //모집글 작성
        RecruitmentCreateResponseDto recruitmentCreateResponseDto = recruitmentService.createRecruitment("test0", recruitment);

        List<Member> member = memberRepository.findAll();
        List<Long> array=new ArrayList<>();
        //모집글에 참가신청
        for (Member member1 : member) {
            if(!member1.getUsername().equals("test0")){
                recruitmentApplyService.apply(member1.getUsername(), recruitmentCreateResponseDto.getId(), applyDto);
                array.add(member1.getId());
            }
        }

        //when

        CreatTeamRequestDto createTeamDto = CreatTeamRequestDto.builder()
                .endDate("2023-08-18 21:46:50")
                .startDate("2023-07-18 21:46:50")
                .teamName("삼백사점")
                .maxTeamSize(5L)
                .recruitId(recruitmentCreateResponseDto.getId())
                .selectedUserIds(array)
                .build();
        createdTeamResponseDto result = teamService.createTeam("test0", createTeamDto);


        //then
        Assertions.assertThat(result.getCurrentSize()).isEqualTo( array.size()+1);



    }
    @Test
    public void 팀삭제(){

        //given
        셋업멤버();
        Long boardId = 공모전생성();

        RecruitmentCreateRequestDto recruitment = RecruitmentCreateRequestDto.builder()
                .title("모집글 제목입니다.")
                .content("저랑 같이 팀하실분 구합니다")
                .currentMemberSize(0)
                .maxMemberSize(5)
                .boardId(boardId)
                .build();

        CreateApplyRequest applyDto = CreateApplyRequest.builder()
                .content("저 팀에 참여하고싶습니다 받아주세요 ")
                .build();
        //모집글 작성
        RecruitmentCreateResponseDto recruitmentCreateResponseDto = recruitmentService.createRecruitment("test0", recruitment);

        List<Member> member = memberRepository.findAll();
        List<Long> array=new ArrayList<>();
        //모집글에 참가신청
        for (Member member1 : member) {
            if(!member1.getUsername().equals("test0")){
                recruitmentApplyService.apply(member1.getUsername(), recruitmentCreateResponseDto.getId(), applyDto);
                array.add(member1.getId());
            }
        }

        //when

        CreatTeamRequestDto createTeamDto = CreatTeamRequestDto.builder()
                .endDate("2023-08-18 21:46:50")
                .startDate("2023-07-18 21:46:50")
                .teamName("삼백사점")
                .maxTeamSize(5L)
                .recruitId(recruitmentCreateResponseDto.getId())
                .selectedUserIds(array)
                .build();
        createdTeamResponseDto result = teamService.createTeam("test0", createTeamDto);


        teamService.deleteTeam("test0",result.getTeamId());

        List<Team> deletedTeam = teamRepository.findAll();

        //then

        Assertions.assertThat(deletedTeam.stream().count()).isEqualTo(0);



    }



    @Test
    public void 팀원삭제(){

        //given
        셋업멤버();
        Long boardId = 공모전생성();

        RecruitmentCreateRequestDto recruitment = RecruitmentCreateRequestDto.builder()
                .title("모집글 제목입니다.")
                .content("저랑 같이 팀하실분 구합니다")
                .currentMemberSize(0)
                .maxMemberSize(5)
                .boardId(boardId)
                .build();

        CreateApplyRequest applyDto = CreateApplyRequest.builder()
                .content("저 팀에 참여하고싶습니다 받아주세요 ")
                .build();
        //모집글 작성
        RecruitmentCreateResponseDto recruitmentCreateResponseDto = recruitmentService.createRecruitment("test0", recruitment);

        List<Member> member = memberRepository.findAll();
        List<Long> array=new ArrayList<>();
        //모집글에 참가신청
        for (Member member1 : member) {
            if(!member1.getUsername().equals("test0")){
                recruitmentApplyService.apply(member1.getUsername(), recruitmentCreateResponseDto.getId(), applyDto);
                array.add(member1.getId());
            }
        }

        //when

        CreatTeamRequestDto createTeamDto = CreatTeamRequestDto.builder()
                .endDate("2023-08-18 21:46:50")
                .startDate("2023-07-18 21:46:50")
                .teamName("삼백사점")
                .maxTeamSize(5L)
                .recruitId(recruitmentCreateResponseDto.getId())
                .selectedUserIds(array)
                .build();
        createdTeamResponseDto result = teamService.createTeam("test0", createTeamDto);



        //usenrame,teamId,memberId
        teamService.deleteTeamMember("test0",result.getTeamId(),3L);
//
//
//        List<MemberTeam> deletedMemberTeam = memberTeamRepository.findAll();
//
//


    }





    @Test
    public void 모집글생성(){
        셋업멤버();
        Long boardId = 공모전생성();

        RecruitmentCreateRequestDto recruitment = RecruitmentCreateRequestDto.builder()
                .title("모집글 제목입니다.")
                .content("저랑 같이 팀하실분 구합니다")
                .currentMemberSize(0)
                .maxMemberSize(0)
                .boardId(boardId)
                .build();

        RecruitmentCreateResponseDto result = recruitmentService.createRecruitment("test0", recruitment);

        Assertions.assertThat(result).isNotNull();


    }

    @Test
    public void 내가속한_팀조회(){
        //given
        셋업멤버();
        Long boardId = 공모전생성();

        RecruitmentCreateRequestDto recruitment = RecruitmentCreateRequestDto.builder()
                .title("모집글 제목입니다.")
                .content("저랑 같이 팀하실분 구합니다")
                .currentMemberSize(0)
                .maxMemberSize(5)
                .boardId(boardId)
                .build();

        CreateApplyRequest applyDto = CreateApplyRequest.builder()
                .content("저 팀에 참여하고싶습니다 받아주세요 ")
                .build();
        //모집글 작성
        RecruitmentCreateResponseDto recruitmentCreateResponseDto = recruitmentService.createRecruitment("test0", recruitment);

        List<Member> member = memberRepository.findAll();
        List<Long> array=new ArrayList<>();
        //모집글에 참가신청
        for (Member member1 : member) {
            if(!member1.getUsername().equals("test0")){
                recruitmentApplyService.apply(member1.getUsername(), recruitmentCreateResponseDto.getId(), applyDto);
                array.add(member1.getId());
            }
        }

        //when

        CreatTeamRequestDto createTeamDto = CreatTeamRequestDto.builder()
                .endDate("2023-08-18 21:46:50")
                .startDate("2023-07-18 21:46:50")
                .teamName("삼백사점")
                .maxTeamSize(5L)
                .recruitId(recruitmentCreateResponseDto.getId())
                .selectedUserIds(array)
                .build();
        createdTeamResponseDto result = teamService.createTeam("test0", createTeamDto);



        List<TeamResponseDto.GetMyTeamResponseDto> getMyTeamResponseDtos = teamService.getMyTeam("test0");
        //Assertion

        // 조회한 팀 리스트가 비어있지 않은지 확인
        Assert.assertFalse(getMyTeamResponseDtos.isEmpty());

        // 조회한 팀 정보와 기대하는 팀 정보를 비교하여 검증
        for (TeamResponseDto.GetMyTeamResponseDto team : getMyTeamResponseDtos) {
            Assert.assertEquals(LocalDateTime.of(2023,8,18,21,46,50), team.getEndDate());
            Assert.assertEquals(LocalDateTime.of(2023,7,18,21,46,50), team.getStartDate());
            Assert.assertEquals("삼백사점", team.getTeamName());
            Assert.assertEquals(boardId,team.getBoardId());

            // 팀 멤버 리스트가 기대하는 멤버 리스트와 동일한지 확인
            List<Long> expectedMemberIds = member.stream().map(Member::getId).collect(Collectors.toList());
            List<Long> actualMemberIds = team.getMemberIds();
            System.out.println(actualMemberIds);
            Assert.assertEquals(expectedMemberIds,actualMemberIds);
        }

    }

    public Long 공모전생성(){

        BoardRequestDto.createBoardResquestDto build = BoardRequestDto.createBoardResquestDto.builder()
                .activitiyName("test1")
                .activityUrl("test")
                .activityImg("test")
                .activitiyDetail("test")
                .category("test")
                .companyType("test")
                .target("test")
                .activityArea("test")
                .recruitmentPeriod("test")
                .recruitmentCount("test")
                .meetingTime("test")
                .homepage("test")
                .activityBenefits("test")
                .interestArea("test")
                .activityField("test")
                .prizeScale("test")
                .competitionCategory("test")
                .preferredSkills("test")
                .activityPeriod("test")
                .build();
        BoardResponseDto.savedBoardIdResponseDto board = boardService.createBoard(build, "test0");

        return board.getBoardId();

    }

    public void 셋업멤버() {

        for(int i=0;i<3;i++) {
            memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                    .username("test"+i)
                    .password("1234")
                    .email("asdas@gmail.com")
                    .phone("010-0000-0000")
                    .profileIntro("나는 훌륭한 일꾼입니다")
                    .profileImage("dd.image")
                    .job(Job.valueOf("COLLEGE"))
                    .education(Education.valueOf("COLLEGE"))
                    .educationGrade(4)
                    .birth(LocalDate.parse("2010-10-11"))
                    .address("경기도 시흥 아몰라 우리집")
                    .gender(Gender.valueOf("MALE"))
                    .kakaoId("kakaoId")
                    .contactEmail("asdas@gmail.com")
                    .isPublic(Long.valueOf("1023"))
                    .usernameChecked(true)
                    .emailChecked(true)
                    .build());
        }
    }


}