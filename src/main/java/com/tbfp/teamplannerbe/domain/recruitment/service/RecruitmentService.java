package com.tbfp.teamplannerbe.domain.recruitment.service;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public Page<RecruitmentSearchDto> getListWithCondition(RecruitmentSearchCondition recruitmentSearchCondition, Pageable pageable) {
        return recruitmentRepository.searchPage(recruitmentSearchCondition, pageable);
    }

    @Transactional
    public RecruitmentCreateResponseDto createRecruitment(String username, RecruitmentCreateRequestDto recruitmentCreateRequestDto) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.UNAUTHORIZED));
        Board board = boardRepository.findByBoardId(recruitmentCreateRequestDto.getBoardId()).orElseThrow(() -> new ApplicationException(ApplicationErrorType.NOT_FOUND));
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
}
