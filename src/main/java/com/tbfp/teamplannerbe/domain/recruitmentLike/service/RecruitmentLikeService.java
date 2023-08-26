package com.tbfp.teamplannerbe.domain.recruitmentLike.service;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.distributeLock.DistributeLock;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.service.RecruitmentService;
import com.tbfp.teamplannerbe.domain.recruitmentLike.dto.RecruitmentLikeResponseDto;
import com.tbfp.teamplannerbe.domain.recruitmentLike.entity.RecruitmentLike;
import com.tbfp.teamplannerbe.domain.recruitmentLike.repository.RecruitmentLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class RecruitmentLikeService {
    private final RecruitmentService recruitmentService;
    private final MemberService memberService;
    private final RecruitmentLikeRepository recruitmentLikeRepository;
    private final MemberRepository memberRepository;

//    @Transactional

    @DistributeLock(key="#recruitmentId")
    public RecruitmentLikeResponseDto.RecruitmentLikeSuccessResponseDto like(Long recruitmentId, String username) {
        // check already liked?
//      pessimistic write lock version is faster twice as much distribute lock(redisson RLock)
        Member member = memberService.findMemberByUsername(username)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));

        Recruitment recruitment = recruitmentService.findById(recruitmentId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
//        Recruitment recruitment = recruitmentService.findById(recruitmentId)
//                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));

        Optional<RecruitmentLike> recruitmentLikeOptional = recruitmentLikeRepository.findByMemberAndRecruitment(member, recruitment);

        if (recruitmentLikeOptional.isPresent()) throw new ApplicationException(ApplicationErrorType.ALREADY_LIKED);

        // like
        RecruitmentLike recruitmentLike = recruitmentLikeRepository.save(
                RecruitmentLike.builder()
                        .recruitment(recruitment)
                        .member(member)
                        .build()
        );
        recruitmentService.increaseLikeCount(recruitment.getId());
/*
        RLock lock = redissonClient.getLock(String.valueOf(recruitmentId));
        try {
            String name = Thread.currentThread().getName();

            if (!lock.tryLock(100, 100, TimeUnit.SECONDS)) {
                log.info("thread name = " + name + ": tryLock failed");
                throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);

            }
            log.info("thread name = " + name + ": acquired");

            Member member = memberService.findMemberByUsername(username)
                    .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
            Recruitment recruitment = recruitmentService.findById(recruitmentId)
                    .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
            Optional<RecruitmentLike> recruitmentLikeOptional = recruitmentLikeRepository.findByMemberAndRecruitment(member, recruitment);

            if (recruitmentLikeOptional.isPresent()) throw new ApplicationException(ApplicationErrorType.ALREADY_LIKED);

            // like
            recruitmentLikeRepository.save(
                    RecruitmentLike.builder()
                            .recruitment(recruitment)
                            .member(member)
                            .build()
            );
            recruitmentService.increaseLikeCount(recruitment.getId());
            log.info("thread name = " + name + ": finished");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            String name = Thread.currentThread().getName();
            if(lock != null && lock.isLocked()) {
                lock.unlock();
                log.info("thread name = " + name + ": unlock");
            }
        }*/
        return RecruitmentLikeResponseDto.RecruitmentLikeSuccessResponseDto.builder()
                .recruitmentLikeId(recruitmentLike.getId())
                .build();
    }


    @Transactional
    public void cancel(Long recruitmentId,String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));
        RecruitmentLike recruitmentLike = recruitmentLikeRepository.findByMemberIdAndRecruitmentId(member.getId(), recruitmentId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        recruitmentLikeRepository.deleteById(recruitmentLike.getId());
        recruitmentService.decreaseLikeCount(recruitmentId);
    }
}
