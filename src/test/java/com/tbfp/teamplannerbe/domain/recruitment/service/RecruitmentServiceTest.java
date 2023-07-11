package com.tbfp.teamplannerbe.domain.recruitment.service;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.recruitment.condition.RecruitmentSearchCondition;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.RecruitmentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.RecruitmentUpdateRequestDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto.RecruitmentCreateResponseDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto.RecruitmentReadResponseDto;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
class RecruitmentServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    RecruitmentService recruitmentService;

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    List<Member> memberList = new ArrayList<>();
    List<Board> boardList = new ArrayList<>();
    List<Recruitment> recruitmentList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        int num = 5;

        for (int i = 0; i < num; ++i) {
            memberList.add(Member.builder().username("member" + i).password(new BCryptPasswordEncoder().encode("1234")).build());
            boardList.add(Board.builder().activityName("board name" + i).build());
        }

        for (int i = 0; i < num; ++i) {
            recruitmentList.add(Recruitment.builder()
                            .viewCount(i)
                            .likeCount(i)
                    .author(memberList.get(i)).board(boardList.get(i % 2)).title("recruitment title" + i).content("recruitment content" + i).build());
        }

        for (int i = 0; i < num; ++i) {
            em.persist(memberList.get(i));
            em.persist(boardList.get(i));
            em.persist(recruitmentList.get(i));
        }
    }

    @Test
    void getListWithConditionTest() {
        // given
        RecruitmentSearchCondition cond = new RecruitmentSearchCondition();
        cond.setBoardTitleContain("0");
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<RecruitmentRequestDto.RecruitmentSearchDto> listWithCondition = recruitmentService.getListWithCondition(cond, pageRequest);

        // then
        // 0, 2, 4
        assertThat(listWithCondition.getContent().stream().map(RecruitmentRequestDto.RecruitmentSearchDto::getTitle)).containsExactly(
                recruitmentList.get(0).getTitle(),
                recruitmentList.get(2).getTitle(),
                recruitmentList.get(4).getTitle()
        );
    }


    @Test
    public void createReadTest() {
        // given
        Member member1 = Member.builder()
                .username("member123")
                .state(true)
                .build();
        Board board = Board.builder()
                .activityName("board activity name")
                .build();
        memberRepository.save(member1);
        boardRepository.save(board);

        RecruitmentCreateRequestDto recruitmentCreateRequestDto = new RecruitmentCreateRequestDto("title123", "content", 10, 100, board.getId());
        RecruitmentCreateResponseDto recruitmentCreateResponseDto = recruitmentService.createRecruitment(member1.getUsername(), recruitmentCreateRequestDto);
        // when
        RecruitmentReadResponseDto recruitmentReadResponseDto = recruitmentService.getOne(recruitmentCreateResponseDto.getId());
        // then
        assertThat(recruitmentReadResponseDto.getTitle()).isEqualTo("title123");
    }

    @Test
    public void createDeleteTest() {
        // given
        Member member1 = Member.builder()
                .username("member123")
                .state(true)
                .build();
        Board board = Board.builder()
                .activityName("board activity name")
                .build();
        memberRepository.save(member1);
        boardRepository.save(board);

        RecruitmentCreateRequestDto recruitmentCreateRequestDto = new RecruitmentCreateRequestDto("title123", "content", 10, 100, board.getId());

        // when
        RecruitmentCreateResponseDto recruitmentCreateResponseDto = recruitmentService.createRecruitment(member1.getUsername(), recruitmentCreateRequestDto);
        Long recruitmentId = recruitmentCreateResponseDto.getId();

        recruitmentService.deleteOne(member1.getUsername(), recruitmentId);
        // then
        assertThatThrownBy(() -> recruitmentService.getOne(recruitmentId))
                .isInstanceOf(ApplicationException.class);
    }
    @Test
    public void createUpdateTest() {
        // given
        Member member1 = Member.builder()
                .username("member123")
                .state(true)
                .build();
        Board board = Board.builder()
                .activityName("board activity name")
                .build();
        memberRepository.save(member1);
        boardRepository.save(board);
        RecruitmentCreateRequestDto recruitmentCreateRequestDto = new RecruitmentCreateRequestDto("title123", "content", 10, 100, board.getId());
        RecruitmentCreateResponseDto recruitmentCreateResponseDto = recruitmentService.createRecruitment(member1.getUsername(), recruitmentCreateRequestDto);
        // when
        RecruitmentUpdateRequestDto recruitmentUpdateRequestDto = new RecruitmentUpdateRequestDto(
            "new title",
                "new content",
                10,
                100
        );
        Long createdRecruitmentId = recruitmentCreateResponseDto.getId();
        recruitmentService.updateOne("member123", createdRecruitmentId, recruitmentUpdateRequestDto);

        // then
        RecruitmentReadResponseDto recruitmentReadResponseDto = recruitmentService.getOne(createdRecruitmentId);
        assertThat(recruitmentReadResponseDto.getTitle()).isEqualTo("new title");
        assertThat(recruitmentReadResponseDto.getContent()).isEqualTo("new content");
    }
}