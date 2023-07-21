package com.tbfp.teamplannerbe.domain.boardLike.service.impl;

import com.tbfp.teamplannerbe.common.BaseControllerTest;
import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import com.tbfp.teamplannerbe.domain.boardLike.service.BoardLikeService;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest
class BoardLikeServiceImplTest extends BaseControllerTest {

    @Autowired
    BoardService boardService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardLikeService boardLikeService;
    @Autowired
    EntityManager em;

    @Test
    @Transactional
    public void 로그인_공모전_좋아요_조회수() throws Exception {

        //회원가입 후 로그인
        셋업멤버();
        Optional<Member> user = memberRepository.findMemberByUsername("test");

        //공모전 생성
        Long boardId = 공모전생성();
        Optional<Board> board = boardRepository.findById(boardId);

        //좋아요 하기 전 카운트
        Long beforeLikeCount = board.get().getLikeCount();


        boardService.getBoardDetail(boardId);
        em.flush();
        em.clear();


        //좋아요 호출
        boardLikeService.createLikesOnBoard(boardId,user.get().getId());
        em.flush();
        em.clear();

        Optional<Board> updatedBoard = boardRepository.findById(boardId);

        Long afterLikeCount = updatedBoard.get().getLikeCount();
        Assertions.assertThat(beforeLikeCount+1).isEqualTo(afterLikeCount);


    }

    @Test
    @Transactional
    public void 로그인_공모전_조회수() throws Exception {

        //회원가입 후 로그인
        셋업멤버();

        //공모전 생성
        Long boardId = 공모전생성();
        Optional<Board> board = boardRepository.findById(boardId);


        //조회 하기 전 조회수 카운트
        Long beforeViewCount = board.get().getView();

        boardService.getBoardDetail(boardId);
        em.flush();
        em.clear();



        Optional<Board> updatedBoard = boardRepository.findById(boardId);

        Long afterViewCount = updatedBoard.get().getView();
        Assertions.assertThat(beforeViewCount+1).isEqualTo(afterViewCount);


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
            BoardResponseDto.savedBoardIdResponseDto board = boardService.createBoard(build, "test");

        return board.getBoardId();

    }


    public void 공모전수정(Long id){

            BoardRequestDto.updateBoardReqeustDto updateDto = BoardRequestDto.updateBoardReqeustDto.builder()
                    .activitiyName("test2"+id)
                    .activityUrl("test2"+id)
                    .activityImg("test2"+id)
                    .activitiyDetail("test2"+id)
                    .category("test2"+id)
                    .companyType("test2"+id)
                    .target("test2"+id)
                    .activityArea("test2"+id)
                    .recruitmentPeriod("test2"+id)
                    .recruitmentCount("test2"+id)
                    .meetingTime("test2"+id)
                    .homepage("test2"+id)
                    .activityBenefits("test2"+id)
                    .interestArea("test2"+id)
                    .activityField("test2"+id)
                    .prizeScale("test2"+id)
                    .competitionCategory("test2"+id)
                    .preferredSkills("test2"+id)
                    .activityPeriod("test2"+id)
                    .build();
            boardService.updateBoard(id,updateDto,"test");


    }

    public void 셋업멤버() {

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                .username("test")
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