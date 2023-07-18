package com.tbfp.teamplannerbe.domain.recruitmentApply.service.impl;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.service.RecruitmentService;
import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyRequestDto.*;
import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyResponseDto.*;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApply;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApplyStateEnum;
import com.tbfp.teamplannerbe.domain.recruitmentApply.repository.RecruitmentApplyRepository;
import com.tbfp.teamplannerbe.domain.recruitmentApply.service.RecruitmentApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecruitmentApplyServiceImpl implements RecruitmentApplyService {

    private final RecruitmentApplyRepository recruitmentApplyRepository;
    private final RecruitmentService recruitmentService;
    private final MemberService memberService;
    @Transactional
    public CreateApplyResponse apply(String username, Long recruitmentId, CreateApplyRequest createApplyRequest) {

        // 이미 신청한 공고에 대한 recruitment?
//        Member member = memberService.findMemberByUsernameOrElseThrowApplicationException(username);
//        Recruitment recruitment = recruitmentService.findByIdOrElseThrowApplicationException(recruitmentId);
//        // 이 신청의 모집글의 공고 가 중복이 없어야함
//        Long boardId = recruitment.getBoard().getId();
//        // member id 로 찾은
//        List<RecruitmentApply> raList = recruitmentApplyRepository.findAllByApplicantFetchRecruitmentFetchBoard(member.getId());
//        // 이미 신청한 공고 체크
//        if (raList.stream().map(ra -> ra.getRecruitment().getBoard().getId()).anyMatch(boardId::equals))
//            return new ApplicationException(ApplicationErrorType.);

        // 이 recruitment 신청 했는지만 체크
        recruitmentApplyRepository.findRecruitmentApplyByRecruitment_IdAndApplicant_Username(recruitmentId, username)
                .ifPresent(
                        s -> {
                            throw new ApplicationException(ApplicationErrorType.RECRUITMENT_APPLY_ALREADY_APPLIED);
                        }
                );

        // not applied
        Member member = memberService.findMemberByUsernameOrElseThrowApplicationException(username);
        Recruitment recruitment = recruitmentService.findByIdOrElseThrowApplicationException(recruitmentId);

        return CreateApplyResponse.builder()
                .recruitmentApplyId(
                        recruitmentApplyRepository.save(
                                RecruitmentApply.builder()
                                        .state(RecruitmentApplyStateEnum.STAND_BY)
                                        .content(createApplyRequest.getContent())
                                        .applicant(member)
                                        .recruitment(recruitment)
                                        .build()

                        ).getId()
                )
                .build();
    }
    @Transactional
    public void cancelApply(String username, Long recruitmentId) {
        RecruitmentApply recruitmentApply = recruitmentApplyRepository.findRecruitmentApplyByRecruitment_IdAndApplicant_Username(recruitmentId, username)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.RECRUITMENT_APPLY_NOT_APPLIED));
        recruitmentApplyRepository.deleteById(recruitmentApply.getId());
    }
}
