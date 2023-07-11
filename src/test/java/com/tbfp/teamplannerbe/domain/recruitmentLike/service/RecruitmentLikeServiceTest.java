package com.tbfp.teamplannerbe.domain.recruitmentLike.service;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.repository.RecruitmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecruitmentLikeServiceTest {

    @Autowired
    RecruitmentLikeService recruitmentLikeService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Test
    void like() {
        // given
        Member member = Member.builder()
                .username("member123")
                .state(true)
                .build();
        Board board = Board.builder()
                .activityName("board activity name")
                .build();
        memberRepository.save(member);
        boardRepository.save(board);
        Recruitment recruitment = Recruitment.builder()
                .title("recruitment title")
                .board(board)
                .author(member)
                .build();
        recruitmentRepository.save(recruitment);
        Integer beforeLikeCount = recruitment.getLikeCount();

        // when
        recruitmentLikeService.like(recruitment.getId(), member.getUsername());

        // then
        Recruitment recruitment1 = recruitmentRepository.findById(recruitment.getId()).get();
        assertThat(recruitment1.getLikeCount()).isEqualTo(beforeLikeCount + 1);
    }

    @Test
    void likeTwice() {
        // given
        Member member = Member.builder()
                .username("member123")
                .state(true)
                .build();
        Board board = Board.builder()
                .activityName("board activity name")
                .build();
        memberRepository.save(member);
        boardRepository.save(board);
        Recruitment recruitment = Recruitment.builder()
                .title("recruitment title")
                .board(board)
                .author(member)
                .build();
        recruitmentRepository.save(recruitment);

        // when
        recruitmentLikeService.like(recruitment.getId(), member.getUsername());

        // then
        assertThrows(ApplicationException.class, () -> recruitmentLikeService.like(recruitment.getId(), member.getUsername()));
    }
}