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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .usernameChecked(true)
                .emailChecked(true)
                .build());
    }

    public String 로그인성공(String id,String password) throws Exception {
        String username = id;
        String rawPassword = password;
        AtomicReference<String> accessToken= new AtomicReference<>("");
        MemberRequestDto.MemberLoginRequestDto memberLoginRequestDto = new MemberRequestDto.MemberLoginRequestDto(username, rawPassword);

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
        );

        resultActions.andExpect(status().isOk())
                .andExpect(result -> System.out.println("result = " + result))
                .andExpect(
                        result -> {
                            accessToken.set(result.getResponse().getCookie("accessToken").getValue());
                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("accessToken").getValue())).isEqualTo(username);
                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("refreshToken").getValue())).isEqualTo(username);
                        }
                );

        return accessToken.get();

    }
}