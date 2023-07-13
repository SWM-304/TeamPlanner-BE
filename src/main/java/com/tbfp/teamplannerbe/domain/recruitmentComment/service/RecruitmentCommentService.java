package com.tbfp.teamplannerbe.domain.recruitmentComment.service;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.service.RecruitmentService;
import com.tbfp.teamplannerbe.domain.recruitmentComment.dto.RecruitmentCommentRequestDto.*;
import com.tbfp.teamplannerbe.domain.recruitmentComment.dto.RecruitmentCommentResponseDto.*;
import com.tbfp.teamplannerbe.domain.recruitmentComment.entity.RecruitmentComment;
import com.tbfp.teamplannerbe.domain.recruitmentComment.repository.RecruitmentCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentCommentService {
    private final RecruitmentCommentRepository recruitmentCommentRepository;
    private final MemberService memberService;
    private final RecruitmentService recruitmentService;

    @Transactional
    public CreateResponseDto createComment(String username, CreateRequestDto createRequestDto) {
        Member member = memberService.findMemberByUsernameOrElseThrowApplicationException(username);
        Recruitment recruitment = recruitmentService.findByIdOrElseThrowApplicationException(createRequestDto.getRecruitmentId());


        return CreateResponseDto.builder()
                .recruitmentCommentId(
                        recruitmentCommentRepository.save(
                                RecruitmentComment.builder()
                                        .content(createRequestDto.getContent())
                                        .member(member)
                                        .recruitment(recruitment)
                                        .isConfidential(createRequestDto.getIsConfidential())
                                        .isDeleted(false)
                                        .parentComment(null)
                                        .build()
                        ).getId()
                )
                .build();
    }

    @Transactional
    public UpdateResponseDto updateComment(String username, Long recruitmentCommentId, UpdateRequestDto updateRequestDto) {
        RecruitmentComment recruitmentComment = checkPrincipalAuthorizedAndReturnRecruitmentComment(username, recruitmentCommentId);
        recruitmentComment.updateContent(updateRequestDto.getNewContent());
        return UpdateResponseDto.builder()
                .recruitmentCommentId(recruitmentComment.getId())
                .build();
    }


    private RecruitmentComment checkPrincipalAuthorizedAndReturnRecruitmentComment(String username, Long recruitmentCommentId) {
        Member member = memberService.findMemberByUsernameOrElseThrowApplicationException(username);

        // check comment is written by member
        RecruitmentComment recruitmentComment = recruitmentCommentRepository.findByIdFetchMember(recruitmentCommentId).orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        if (!recruitmentComment.getMember().getUsername().equals(member.getUsername()))
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);
        return recruitmentComment;
    }

    @Transactional
    public DeleteResponseDto deleteComment(String username, Long recruitmentCommentId) {
        RecruitmentComment recruitmentComment = checkPrincipalAuthorizedAndReturnRecruitmentComment(username, recruitmentCommentId);
        recruitmentComment.makeDeleted();
        return DeleteResponseDto.builder()
                .recruitmentCommentId(recruitmentComment.getId())
                .build();
    }

    // 대댓글 달 수 있는 조건?
    // 내가 그 댓글 볼 수 있어야함 ( 내가 쓴 댓글이거나 남이 썻는데 confidential 이 아님)
    // 또는 내가 모집글의 작성자임
    @Transactional
    public CreateCoCommentResponseDto createCommentToComment(String username, Long parentRecruitmentCommentId, CreateCoCommentRequestDto createCoCommentRequestDto) {
        Recruitment recruitment = recruitmentService.findByIdOrElseThrowApplicationException(createCoCommentRequestDto.getRecruitmentId());
        Member member = memberService.findMemberByUsernameOrElseThrowApplicationException(username);
        RecruitmentComment parentRecruitmentComment = recruitmentCommentRepository.findById(parentRecruitmentCommentId).orElseThrow(() -> new ApplicationException(ApplicationErrorType.RECRUITMENT_COMMENT_NOT_FOUND));

        // condition check
        if ( !
                (
                        recruitment.getAuthor().getUsername().equals(member.getUsername())
                    || (!parentRecruitmentComment.isConfidential() || parentRecruitmentComment.getMember().getUsername().equals(username))
                )
            )
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);

        // can comment to this comment
        return CreateCoCommentResponseDto.builder()
                .recruitmentCommentId(
                        recruitmentCommentRepository.save(
                                RecruitmentComment.builder()
                                        .content(createCoCommentRequestDto.getContent())
                                        .member(member)
                                        .recruitment(recruitment)
                                        .isConfidential(createCoCommentRequestDto.getIsConfidential())
                                        .isDeleted(false)
                                        .parentComment(parentRecruitmentComment)
                                        .build()
                        ).getId()
                )
                .build();
    }

    private RecruitmentComment checkPrincipalAuthorizedAndReturnRecruitmentComment(Member member, Long recruitmentCommentId) {
        RecruitmentComment recruitmentComment = recruitmentCommentRepository.findByIdFetchMember(recruitmentCommentId).orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        if (!recruitmentComment.getMember().getUsername().equals(member.getUsername()))
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);
        return recruitmentComment;
    }
}
