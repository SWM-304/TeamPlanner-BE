package com.tbfp.teamplannerbe.domain.recruitmentLike.service;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.service.RecruitmentService;
import com.tbfp.teamplannerbe.domain.recruitmentLike.entity.RecruitmentLike;
import com.tbfp.teamplannerbe.domain.recruitmentLike.repository.RecruitmentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecruitmentLikeService {
    private final RecruitmentService recruitmentService;
    private final MemberService memberService;
    private final RecruitmentLikeRepository recruitmentLikeRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String like(Long recruitmentId, String username) {
        Member member = memberService.findMemberByUsername(username)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));

        // check already liked?
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
        return "success";
    }

}
