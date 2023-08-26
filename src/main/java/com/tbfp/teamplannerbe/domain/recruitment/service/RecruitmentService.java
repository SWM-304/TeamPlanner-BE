package com.tbfp.teamplannerbe.domain.recruitment.service;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.profile.service.ProfileService;
import com.tbfp.teamplannerbe.domain.recruitment.condition.RecruitmentSearchCondition;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.*;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto.*;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final ProfileService profileService;

    public Page<RecruitmentSearchDto> getListWithCondition(RecruitmentSearchCondition recruitmentSearchCondition, Pageable pageable) {
        return recruitmentRepository.searchPage(recruitmentSearchCondition, pageable);
    }

    @Transactional
    public RecruitmentCreateResponseDto createRecruitment(String username, RecruitmentCreateRequestDto recruitmentCreateRequestDto) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.UNAUTHORIZED));
        Board board = boardRepository.findById(recruitmentCreateRequestDto.getBoardId()).orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        return new RecruitmentCreateResponseDto(
                recruitmentRepository.save(recruitmentCreateRequestDto.toEntity(member, board)).getId()
        );
    }

    @Transactional
    public RecruitmentReadResponseDto getOne(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository
                .findById(recruitmentId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        recruitment.incrementViewCount();
        return RecruitmentReadResponseDto.toDto(
                recruitment
        );
    }

    @Transactional
    public String deleteOne(String username, Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository
                .findById(recruitmentId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        if(!recruitment.getAuthor().getUsername().equals(username))
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);

        recruitmentRepository.deleteById(recruitmentId);
        return "";
    }

    @Transactional
    public String updateOne(String username, Long recruitmentId, RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        Recruitment recruitment = recruitmentRepository
                .findById(recruitmentId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        if(!recruitment.getAuthor().getUsername().equals(username))
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);

        recruitment.update(recruitmentUpdateRequestDto);
        return "";
    }

    public Optional<Recruitment> findById(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId);
    }

    @Transactional
    public void increaseLikeCount(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        recruitment.incrementLikeCount();
    }
    @Transactional
    public void decreaseLikeCount(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
        recruitment.decrementLikeCount();
    }

    public Recruitment findByIdOrElseThrowApplicationException(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new ApplicationException(ApplicationErrorType.RECRUITMENT_NOT_FOUND));
    }

    @Transactional
    public RecruitmentWithCommentResponseDto getOneWithComment(Principal principal, Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findByIdFetchComment(recruitmentId).orElseThrow(() -> new ApplicationException(ApplicationErrorType.RECRUITMENT_NOT_FOUND));
//        Member member = memberService.findMemberByUsernameOrElseThrowApplicationException(username);
//        String profileImage = profileService.getBasicProfile(username).getProfileImage();

//        boolean isAuthorOfRecruitment = recruitment.getAuthor().getUsername().equals(member.getUsername());
        String username = principal == null ? null : principal.getName();
        boolean isAuthorOfRecruitment = recruitment.getAuthor().getUsername().equals(username);
        recruitment.incrementViewCount();

        return RecruitmentWithCommentResponseDto.toDto(isAuthorOfRecruitment, username, recruitment);
    }

    // pessimistic_write lock
    public Optional<Recruitment> findByIdForUpdate(Long recruitmentId) {
        return recruitmentRepository.findByIdForUpdate(recruitmentId);
    }
}
