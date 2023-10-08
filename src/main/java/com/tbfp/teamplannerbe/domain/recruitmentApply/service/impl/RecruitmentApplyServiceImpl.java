package com.tbfp.teamplannerbe.domain.recruitmentApply.service.impl;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.notification.dto.request.CreateMessageEvent;
import com.tbfp.teamplannerbe.domain.notification.service.NotificationService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecruitmentApplyServiceImpl implements RecruitmentApplyService {

    private final RecruitmentApplyRepository recruitmentApplyRepository;
    private final RecruitmentService recruitmentService;
    private final MemberService memberService;

    private final NotificationService notificationService;
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

        if(recruitment.getAuthor().getUsername().equals(username)){
            throw new ApplicationException(ApplicationErrorType.AUTHOR_CANNOT_PARTICIPATE);
        }
        sendNotification(createApplyRequest, recruitment);

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

    private void sendNotification(CreateApplyRequest createApplyRequest, Recruitment recruitment) {
        CreateMessageEvent createMessageEvent = CreateMessageEvent.builder()
                .createdDate(LocalDateTime.now())
                .memberId(recruitment.getAuthor().getId())
                .content(createApplyRequest.getContent())
                .recruitmentProfileImage(recruitment.getBoard().getActivityImg())
                .build();
        notificationService.send(createMessageEvent);
    }

    @Transactional
    public void cancelApply(String username, Long recruitmentId) {
        RecruitmentApply recruitmentApply = recruitmentApplyRepository.findRecruitmentApplyByRecruitment_IdAndApplicant_Username(recruitmentId, username)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.RECRUITMENT_APPLY_NOT_APPLIED));
        recruitmentApplyRepository.deleteById(recruitmentApply.getId());
    }

    @Override
    public List<GetApplyResponse> getApplyList(String username) {
        Optional<Member> member = memberService.findMemberByUsername(username);
        if(member.isEmpty()){
            throw new ApplicationException(ApplicationErrorType.USER_NOT_FOUND);
        }
        List<RecruitmentApply> ra = recruitmentApplyRepository.findAllRecruitmentApplyByApplicant_Username(username);
        return ra.stream().map(ra1 -> GetApplyResponse.toDto(ra1,member.get().getNickname())).collect(Collectors.toList());
    }
}
