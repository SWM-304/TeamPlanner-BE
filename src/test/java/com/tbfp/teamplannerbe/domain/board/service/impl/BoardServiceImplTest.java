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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
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
        Assertions.assertThat(board.stream().count()).isEqualTo(30);

    }

    @Test
    @Transactional
    public void 조회수_테스트() throws Exception {
        셋업멤버();
        공모전생성();
        List<BoardResponseDto.BoardDetailResponseDto> beforeViewCount = boardService.getBoardDetail(30L);
        String accessToken = 로그인성공("test", "1234");

        BoardSimpleListResponseDto responseDto = new BoardSimpleListResponseDto();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("http://localhost:8080/api/v1/board/30")
                        .header("Authorization", "Bearer " + accessToken.toString())
                        .content(objectMapper.writeValueAsString(responseDto))
        );
        List<BoardResponseDto.BoardDetailResponseDto> afterViewCount = boardService.getBoardDetail(30L);

        resultActions.andExpect(status().isOk())
                .andExpect(result -> Assertions.assertThat(beforeViewCount.get(0).getViewCount()+1)).equals(afterViewCount);

    }

    @Test
    @Transactional
    public void 공모전동적검색() throws Exception {

        셋업멤버();
        String accessToken = 로그인성공("test", "1234");

        BoardSimpleListResponseDto responseDto = new BoardSimpleListResponseDto();

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
    public void
    회원가입_공모전수정(){

        셋업멤버();
        공모전생성();
        List<Board> beforeBoard = boardRepository.findAll();
        공모전수정();
        List<Board> afterBoard = boardRepository.findAll();
        Assertions.assertThat(beforeBoard.get(0).getActivityDetail()).isNotEqualTo(afterBoard.get(0).getActivityDetail());

    }

    @Test
    @Transactional
    public void 공모전삭제(){
        셋업멤버();
        공모전생성();

        for(int i=1;i<=30;i++){
            boardRepository.deleteById((long) i);
        }

        List<Board> all = boardRepository.findAll();

        for (Board board : all) {
            System.out.println("너 몇개야"+board.getActivityName());
        }
        Assertions.assertThat(all.stream().count()).isEqualTo(0);

    }




    public void 공모전생성(){


        for(int i=1;i<=30;i++){
            BoardRequestDto.createBoardResquestDto build = BoardRequestDto.createBoardResquestDto.builder()
                    .activitiyName("test1" + i)
                    .activityUrl("test" + i)
                    .activityImg("test" + i)
                    .activitiyDetail("test" + i)
                    .category("test" + i)
                    .companyType("test" + i)
                    .target("test" + i)
                    .activityArea("test" + i)
                    .recruitmentPeriod("test" + i)
                    .recruitmentCount("test" + i)
                    .meetingTime("test" + i)
                    .homepage("test" + i)
                    .activityBenefits("test" + i)
                    .interestArea("test" + i)
                    .activityField("test" + i)
                    .prizeScale("test" + i)
                    .competitionCategory("test" + i)
                    .preferredSkills("test" + i)
                    .activityPeriod("test" + i)
                    .build();
            boardService.createBoard(build,"test");
        }

    }


    public void 공모전수정(){


        for(int i=1;i<=30;i++){
            BoardRequestDto.updateBoardReqeustDto updateDto = BoardRequestDto.updateBoardReqeustDto.builder()
                    .activitiyName("test2" + i)
                    .activityUrl("test2" + i)
                    .activityImg("test2" + i)
                    .activitiyDetail("test2" + i)
                    .category("test2" + i)
                    .companyType("test2" + i)
                    .target("test2" + i)
                    .activityArea("test2" + i)
                    .recruitmentPeriod("test2" + i)
                    .recruitmentCount("test2" + i)
                    .meetingTime("test2" + i)
                    .homepage("test2" + i)
                    .activityBenefits("test2" + i)
                    .interestArea("test2" + i)
                    .activityField("test2" + i)
                    .prizeScale("test2" + i)
                    .competitionCategory("test2" + i)
                    .preferredSkills("test2" + i)
                    .activityPeriod("test2" + i)
                    .build();
            boardService.updateBoard((long) i,updateDto,"test");
        }

    }

    public void 셋업멤버() {

        memberService.registerMember(MemberRequestDto.SignUpRequestDto.builder()
                .loginId("test")
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
    }

    public String 로그인성공(String id,String password) throws Exception {
        String loginId = id;
        String rawPassword = password;
        AtomicReference<String> accessToken= new AtomicReference<>("");
        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto(loginId, rawPassword);

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
        );

        resultActions.andExpect(status().isOk())
                .andExpect(result -> System.out.println("result = " + result))
                .andExpect(
                        result -> {
                            accessToken.set(result.getResponse().getCookie("accessToken").getValue());
                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("accessToken").getValue())).isEqualTo(loginId);
                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("refreshToken").getValue())).isEqualTo(loginId);
                        }
                );

        return accessToken.get();

    }

}