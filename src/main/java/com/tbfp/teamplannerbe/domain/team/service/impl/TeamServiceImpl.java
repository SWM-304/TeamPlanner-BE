package com.tbfp.teamplannerbe.domain.team.service.impl;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.profile.entity.Evaluation;
import com.tbfp.teamplannerbe.domain.profile.repository.EvaluationRepository;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.repository.RecruitmentRepository;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApply;
import com.tbfp.teamplannerbe.domain.recruitmentApply.repository.RecruitmentApplyRepository;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto.createdTeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.MemberTeamRepository;
import com.tbfp.teamplannerbe.domain.team.repository.TeamRepository;
import com.tbfp.teamplannerbe.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;
import static com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApplyStateEnum.ACCEPT;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final RecruitmentApplyRepository recruitmentApplyRepository;
    private final EvaluationRepository evaluationRepository;





    @Transactional
    @Override
    public TeamResponseDto.createdTeamResponseDto createTeam(String username, TeamRequestDto.CreatTeamRequestDto creatTeamRequestDto){

        createdTeamResponseDto result=null;

        //selected 한게 참여신청리스트에 해당 팀원모집글에 대해 신청한적이 있는지 확인 신청 한적이없다면 예외처리
        //병렬 스트림의 경우 여러 스레드에서 요소를 처리하므로, 첫 번째 요소를 찾는 것보다 임의의 요소를 찾는 것이 더 효율
        creatTeamRequestDto.getSelectedUserIds().stream()
                .map(selectedUserId -> recruitmentApplyRepository.findRecruitmentApplyByRecruitment_IdAndApplicant_Id(
                        creatTeamRequestDto.getRecruitId(), selectedUserId
                ).orElseThrow(() -> new ApplicationException(RECRUITMENT_APPLY_NOT_APPLIED)))
                .filter(recruitmentApply -> recruitmentApply.getState() == ACCEPT)
                .findAny()
                .ifPresent(recruitmentApply -> {
                    throw new ApplicationException(ALREADY_TEAM_ACCEPT);
                });


        // 해당하는 모집글이 아직도 존재하는지 확인
        Recruitment recruitment = recruitmentRepository.findById(creatTeamRequestDto.getRecruitId())
                .orElseThrow(() -> new ApplicationException(RECRUITMENT_NOT_FOUND));
        Member leaderMember = memberRepository.findByUsername(username).
                orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));


        //자기 자신을 팀으로 설정하는경우 예외처리 , 팀의 팀장으로 그대로 남아있게함.
        if (creatTeamRequestDto.getSelectedUserIds().contains(leaderMember.getId())) {
            throw new ApplicationException(UNAUTHORIZED);
        }

        // 승인 할 멤버들이 존재하는지 검사
        List<Member> selectedMembers = creatTeamRequestDto.getSelectedUserIds().stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND)))
                .collect(Collectors.toList());

        // 모집글에 대한 팀을 조회
        Team findTeam = teamRepository.findByRecruitmentId(recruitment.getId());


        //기존의 팀이 없음
        if (findTeam == null) {
            // max 인원보다 승인하려는 인원이 더 많은 경우
            if (creatTeamRequestDto.getMaxTeamSize() < creatTeamRequestDto.getSelectedUserIds().stream().count() + 1) {
                throw new ApplicationException(TEAM_CAPACITY_EXCEEDED);
            }

            // 팀생성
            Team team = Team.initialCreateTeam(creatTeamRequestDto, recruitment, username);

            team.saveTeamLeaderToTeamMember(leaderMember);
            //팀 저장
            Team savedTeam = teamRepository.save(team);

            //팀 member 저장
            savedTeam.addTeamMembers(selectedMembers,username);


            // 저장된 멤버들을 순회하면서 recruitmentApply 상태값을 ACCEPT값으로 변경
            selectedMembers.forEach(member -> {
                RecruitmentApply recruitmentApply = recruitmentApplyRepository.findRecruitmentApplyByRecruitment_IdAndApplicant_Id(
                        creatTeamRequestDto.getRecruitId(), member.getId()
                ).orElseThrow(() -> new ApplicationException(RECRUITMENT_APPLY_NOT_APPLIED));

                recruitmentApply.updateState(ACCEPT);
            });

            // 변경된 상태값 저장
            Team updatedTeam = teamRepository.save(savedTeam);

            //Response 반환값 변환
            result = createdTeamResponseDto.toDto(updatedTeam);
        }




        //이전에 팀을 생성한 적이 있으면 더티체킹
        if (findTeam != null) {

            // 팀 최대인원을 초과하는지 확인
            findTeam.checkTeamCapacity(findTeam, creatTeamRequestDto);

            // 팀 정보를 더티체킹함
            findTeam.updateTeam(creatTeamRequestDto);
            // team 정보 저장
            Team chagedTeam = teamRepository.save(findTeam);


            // team에 members 추가
            chagedTeam.addTeamMembers(selectedMembers,username);

            // 저장된 멤버들을 순회하면서 recruitmentApply 상태값을 ACCEPT값으로 변경
            selectedMembers.forEach(member -> {
                RecruitmentApply recruitmentApply = recruitmentApplyRepository.findRecruitmentApplyByRecruitment_IdAndApplicant_Id(
                        creatTeamRequestDto.getRecruitId(), member.getId()
                ).orElseThrow(() -> new ApplicationException(RECRUITMENT_APPLY_NOT_APPLIED));

                recruitmentApply.updateState(ACCEPT);
            });

            // 변경된 상태값 저장
            Team updatedTeam = teamRepository.save(chagedTeam);

            //Response 반환값 변환
            result = createdTeamResponseDto.toDto(updatedTeam);
        }


        return result;
    }


    @Transactional
    public void deleteTeamMember(String username, Long teamId, Long memberId) {

        //팀장만 팀원삭제가능함
        Team team = teamRepository.findByTeamLeader(username)
                .orElseThrow(() -> new ApplicationException(UNAUTHORIZED));
        //해당하는 팀이 있는지 확인
        teamRepository.findById(teamId).
                orElseThrow(() -> new ApplicationException(TEAM_NOT_FOUND));

        //TeamMember테이블에서 삭제시킴
        //해당하는 테이블에 이게없는데 왜 예외가 안터지냐...?
        memberTeamRepository.deleteByTeamIdAndMemberId(teamId,memberId).
                orElseThrow(()->new ApplicationException(TEAM_NOT_FOUND));

        team.decreaseTeamSize();
    }

    @Override
    @Transactional
    public void deleteTeam(String username, Long teamId) {

        //팀장만 팀원삭제가능함
        teamRepository.findByTeamLeader(username)
                .orElseThrow(() -> new ApplicationException(UNAUTHORIZED));
        //해당하는 팀이 있는지 확인
        Team team = teamRepository.findById(teamId).
                orElseThrow(() -> new ApplicationException(TEAM_NOT_FOUND));
        //멤버팀테이블에 teamId인거 싹다 지워야야하고
        List<MemberTeam> memberTeamList = memberTeamRepository.findAllByTeamId(teamId);

        //순회하면서 member하나씩 삭제
        memberTeamList.stream().forEach(
                memberTeam -> memberTeamRepository.deleteByMemberId(memberTeam.getId())
                        .orElseThrow(()->new ApplicationException(USER_NOT_FOUND))
        );
        //memberTeam 다 삭제 됐으면 해당 팀 삭제
        teamRepository.deleteById(teamId);


    }

    @Override
    @Transactional
    public TeamResponseDto.CreateEvaluationResponseDto createEvaluation(TeamRequestDto.CreateEvaluationRequestDto createEvaluationRequestDto, Long givenTeamId, Long subjectMemberId, String username){
        Member authorMember = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Member subjectMember = memberRepository.findById(subjectMemberId).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Evaluation alreadyExistingEvaluation = evaluationRepository.findByAuthorMemberIdAndSubjectMemberId(authorMember.getId(), subjectMemberId).orElse(null);

        //이미 해당 사용자에 대한 평가 작성 완료함
        if(alreadyExistingEvaluation!=null){
            throw new ApplicationException(EVALUATION_ALREADY_EXIST);
        }




        //자가 자신 평가 불가능
        if(authorMember.equals(subjectMember)){
            throw new ApplicationException(UNABLE_TO_EVALUATE_MYSELF);
        }

        //teamId에 해당하는 team 없을 경우
        Team givenTeam = teamRepository.findById(givenTeamId).orElseThrow(() -> new ApplicationException(TEAM_NOT_FOUND));

        //평가자와 피평가자가 주어진 팀에 동시에 속해있지 않을 경우
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdsAndTeamIds(authorMember.getId(), subjectMemberId, givenTeamId);
        if(memberTeam==null) throw new ApplicationException(USER_NOT_IN_TEAM);

        // 평가 점수 총합은 0~30 이어야 함
        Integer sum = createEvaluationRequestDto.getStat1() + createEvaluationRequestDto.getStat2() + createEvaluationRequestDto.getStat3() + createEvaluationRequestDto.getStat4() + createEvaluationRequestDto.getStat5();
        if(sum < 0 || sum > 30) throw new ApplicationException(EVALUATION_SCORE_NOT_IN_SCOPE);

        //save
        Evaluation evaluation = createEvaluationRequestDto.toEntity(authorMember,subjectMember,givenTeam);
        evaluationRepository.save(evaluation);

        return TeamResponseDto.CreateEvaluationResponseDto.builder()
                .message("평가가 완료되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public TeamResponseDto.UpdateEvaluationResponseDto updateEvaluation(TeamRequestDto.UpdateEvaluationRequestDto updateEvaluationRequestDto, Long givenTeamId, Long subjectMemberId, String username){
        Evaluation evaluationToUpdate = evaluationRepository.findById(updateEvaluationRequestDto.getId()).orElse(null);

        //updateEvaluationDto.getId()를 id로 갖는 평가가 없으면 예외
        if(evaluationToUpdate==null){
            throw new ApplicationException(EVALUATION_NOT_EXIST);
        }

        //해당 평가의 작성자가 본인이 아니면 예외
        if(!evaluationToUpdate.getAuthorMember().getUsername().equals(username)){
            throw new ApplicationException(NOT_AUTHOR_OF_EVALUATION);
        }

        Member authorMember = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Member subjectMember = memberRepository.findById(subjectMemberId).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        //자가 자신 평가 불가능
        if(authorMember.equals(subjectMember)){
            throw new ApplicationException(UNABLE_TO_EVALUATE_MYSELF);
        }
        //teamId에 해당하는 team 없을 경우
        Team givenTeam = teamRepository.findById(givenTeamId).orElseThrow(() -> new ApplicationException(TEAM_NOT_FOUND));

        //평가자와 피평가자가 주어진 팀에 동시에 속해있지 않을 경우
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdsAndTeamIds(authorMember.getId(), subjectMemberId, givenTeamId);
        if(memberTeam==null) throw new ApplicationException(USER_NOT_IN_TEAM);

        // 평가 점수 총합은 0~30 이어야 함
        Integer sum = updateEvaluationRequestDto.getStat1() + updateEvaluationRequestDto.getStat2() + updateEvaluationRequestDto.getStat3() + updateEvaluationRequestDto.getStat4() + updateEvaluationRequestDto.getStat5();
        if(sum < 0 || sum > 30) throw new ApplicationException(EVALUATION_SCORE_NOT_IN_SCOPE);

        //save
        Evaluation evaluation = updateEvaluationRequestDto.toEntity(authorMember,subjectMember,givenTeam);
        evaluationRepository.save(evaluation);

        return TeamResponseDto.UpdateEvaluationResponseDto.builder()
                .message("평가가 수정되었습니다.")
                .build();
    }
}
