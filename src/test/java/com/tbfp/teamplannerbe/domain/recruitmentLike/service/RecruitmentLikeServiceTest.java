package com.tbfp.teamplannerbe.domain.recruitmentLike.service;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.repository.RecruitmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecruitmentLikeServiceTest {

    @Autowired
    RecruitmentLikeService recruitmentLikeService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberService memberService;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Test
    @Transactional
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
    @Transactional
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

    Long rid = null;
    String mUsername = null;
    @Transactional
    public void setUp() {
        System.out.println("RecruitmentLikeServiceTest.setUp");
        Member member0 = null;
        for (int i = 0; i < 1000; i++) {
            Member member = Member.builder()
                    .username("member"+i)
                    .state(true)
                    .build();
            memberRepository.save(member);
            if (i == 0) member0 = member;
        }
        Board board = Board.builder()
                .activityName("board activity name")
                .likeCount(0L)
                .view(0L)
                .build();
        mUsername = "member123";
        boardRepository.save(board);
        Recruitment recruitment = Recruitment.builder()
                .title("recruitment title")
                .board(board)
                .author(member0)
                .likeCount(0)
                .viewCount(0)
                .build();
        rid = recruitmentRepository.save(recruitment).getId();
    }
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void multiThreadTest_oneMemberManyLike() throws InterruptedException {
        // given
        setUp();
        // when
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        CountDownLatch latch = new CountDownLatch (10000);
        for (int i = 0; i < 10000; ++i) {
            int finalI = i;
            executorService.submit(() -> {
                String threadName = Thread.currentThread().getName();
                try {
                    recruitmentLikeService.like(rid, mUsername);
                    System.out.println(threadName + " : SUCCESS");
                } catch (ApplicationException e) {
                    System.out.println("finalI = " + finalI + "\nerror = " + e.getErrorType());
                    System.out.println(threadName + " : APPLICATION_EXCEPTION");
//                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println(threadName + " : JUST_EXCEPTION");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Recruitment recruitment1 = recruitmentRepository.findById(rid).get();
        assertThat(recruitment1.getLikeCount()).isEqualTo(1);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void multiThreadTest_ManyMemberOneLikeEach() throws InterruptedException {
        // given
        setUp();
        int threadNum = 100;
        int likeCount = 1000;
        System.out.println("rid = " + rid);
        // when
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch latch = new CountDownLatch (likeCount);
        for (int i = 0; i < likeCount; ++i) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    recruitmentLikeService.like(rid, "member" + finalI);
                    System.out.println("SUCCESS");
                } catch (ApplicationException e) {
                    System.out.println("finalI = " + finalI + "\nerror = " + e.getErrorType());
//                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("finalI = " + finalI + "\nerror = normal Exception");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Recruitment recruitment1 = recruitmentRepository.findById(rid).get();
        assertThat(recruitment1.getLikeCount()).isEqualTo(likeCount);
    }

}
