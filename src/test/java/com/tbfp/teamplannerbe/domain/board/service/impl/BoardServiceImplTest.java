package com.tbfp.teamplannerbe.domain.board.service.impl;

import com.tbfp.teamplannerbe.common.BaseControllerTest;
import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto.BoardSimpleListResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.MemberLoginRequestDto;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class BoardServiceImplTest extends BaseControllerTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    EntityManager em;





    @Test
    @Transactional
    public void 회원가입_공모전생성(){

        셋업멤버();

        공모전생성();
        List<Board> board = boardRepository.findAll();
        Assertions.assertThat(board.stream().count()).isEqualTo(1);

    }

    @Test
    @Transactional
    public void 공모전동적검색() throws Exception {

        셋업멤버();
        String accessToken = 로그인성공("test", "1234");

        BoardSimpleListResponseDto responseDto = null;

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("http://localhost:8080/api/v1/board?category=대외활동&page=0&size=10&sort=likeCount,desc")
                        .header("Authorization", "Bearer " + accessToken.toString())
                        .content(objectMapper.writeValueAsString(responseDto))
        );

        System.out.println(resultActions);

        resultActions.andExpect(status().isOk())
                .andExpect(result -> System.out.println("result = " + result));

    }

    @Test
    @Transactional
    @Rollback // @Transactional을 통해 넣은 데이터를 없던 데이터로(삭제) 하는 어노테이션
    @DisplayName("공모전 수정 테스트")
    public void
    공모전수정(){

        셋업멤버();
        Long boardId = 공모전생성();
        Optional<Board> beforeUpdateBoard = boardRepository.findById(boardId);
        공모전수정(boardId);
        em.flush();
        em.clear();
        Optional<Board> afterUpdateBoard = boardRepository.findById(boardId);

        Assertions.assertThat(beforeUpdateBoard).isNotEqualTo(afterUpdateBoard);


    }

    @Test
    @Transactional
    public void 공모전삭제(){
        셋업멤버();
        Long boardId = 공모전생성();

        boardRepository.deleteById(boardId);
        List<Board> board = boardRepository.findAll();

        Assertions.assertThat(board.stream().count()).isEqualTo(0);

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
                    .activitiyName("test2" + id)
                    .activityUrl("test2" + id)
                    .activityImg("test2" + id)
                    .activitiyDetail("test2" + id)
                    .category("test2" + id)
                    .companyType("test2" + id)
                    .target("test2" + id)
                    .activityArea("test2" + id)
                    .recruitmentPeriod("test2" + id)
                    .recruitmentCount("test2" + id)
                    .meetingTime("test2" + id)
                    .homepage("test2" + id)
                    .activityBenefits("test2" + id)
                    .interestArea("test2" + id)
                    .activityField("test2" + id)
                    .prizeScale("test2" + id)
                    .competitionCategory("test2" + id)
                    .preferredSkills("test2" + id)
                    .activityPeriod("test2" + id)
                    .build();
        boardService.updateBoard(id, updateDto, "test");


    }

    public void 셋업멤버() {

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                .username("test")
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
    }

    public String 로그인성공(String id,String password) throws Exception {
        String username = id;
        String rawPassword = password;
        AtomicReference<String> accessToken= new AtomicReference<>("");
        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto(username, rawPassword);

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