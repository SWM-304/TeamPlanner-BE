package com.tbfp.teamplannerbe.domain.profile.service.impl;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.profile.CRUDType;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileRequestDto;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.*;
import com.tbfp.teamplannerbe.domain.profile.entity.*;
import com.tbfp.teamplannerbe.domain.profile.repository.*;
import com.tbfp.teamplannerbe.domain.profile.service.ProfileService;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.MemberTeamRepository;
import com.tbfp.teamplannerbe.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;
    private final TechStackItemRepository techStackItemRepository;
    private final ActivityRepository activityRepository;
    private final CertificationRepository certificationRepository;
    private final EvaluationRepository evaluationRepository;
    private final BasicProfileRepository basicProfileRepository;
    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;

    @Override
    @Transactional
    public ProfileResponseDto.ShowProfileResponseDto showProfile(String nickname){

        Optional<Member> member = memberRepository.findByNickname(nickname);
        if(!member.isPresent()) throw new ApplicationException(ApplicationErrorType.USER_NOT_FOUND);

        Long memberId = member.get().getId();

        ProfileResponseDto.BasicProfileResponseDto basicProfileResponseDto = basicProfileRepository.findByMemberId(memberId).orElseThrow(()->new ApplicationException(ApplicationErrorType.PROFILE_NOT_EXIST)).toDto();
        List<ProfileResponseDto.TechStackResponseDto> techStackResponseDtos = techStackRepository.findAllByMemberId(memberId).orElse(null).stream().map(TechStack::toDto).collect(Collectors.toList());
        List<ProfileResponseDto.ActivityResponseDto> activityResponseDtos = activityRepository.findAllByMemberId(memberId).orElse(null).stream().map(Activity::toDto).collect(Collectors.toList());
        List<ProfileResponseDto.CertificationResponseDto> certificationResponseDtos = certificationRepository.findAllByMemberId(memberId).orElse(null).stream().map(Certification::toDto).collect(Collectors.toList());
        List<ProfileResponseDto.EvaluationResponseDto> evaluationResponseDtos = evaluationRepository.findAllBySubjectMemberId(memberId).orElse(null).stream().map(Evaluation::toDto).collect(Collectors.toList());

        return ProfileResponseDto.ShowProfileResponseDto.builder()
                .basicProfile(basicProfileResponseDto)
                .techStacks(techStackResponseDtos)
                .activities(activityResponseDtos)
                .certifications(certificationResponseDtos)
                .evaluations(evaluationResponseDtos)
                .build();
    }

    @Override
    @Transactional
    public ProfileResponseDto.GetProfileResponseDto getProfile(String username){

        Optional<Member> member = memberRepository.findMemberByUsername(username);
        if(!member.isPresent()) throw new ApplicationException(ApplicationErrorType.USER_NOT_FOUND);

        Long memberId = member.get().getId();

        ProfileResponseDto.BasicProfileResponseDto basicProfileResponseDto = basicProfileRepository.findByMemberId(memberId).orElseThrow(()->new ApplicationException(ApplicationErrorType.PROFILE_NOT_EXIST)).toDto();
        List<ProfileResponseDto.TechStackResponseDto> techStackResponseDtos = techStackRepository.findAllByMemberId(memberId).orElse(null).stream().map(TechStack::toDto).collect(Collectors.toList());
        List<ProfileResponseDto.ActivityResponseDto> activityResponseDtos = activityRepository.findAllByMemberId(memberId).orElse(null).stream().map(Activity::toDto).collect(Collectors.toList());
        List<ProfileResponseDto.CertificationResponseDto> certificationResponseDtos = certificationRepository.findAllByMemberId(memberId).orElse(null).stream().map(Certification::toDto).collect(Collectors.toList());
        List<ProfileResponseDto.EvaluationResponseDto> evaluationResponseDtos = evaluationRepository.findAllBySubjectMemberId(memberId).orElse(null).stream().map(Evaluation::toDto).collect(Collectors.toList());
        return ProfileResponseDto.GetProfileResponseDto.builder()
                .basicProfile(basicProfileResponseDto)
                .techStacks(techStackResponseDtos)
                .activities(activityResponseDtos)
                .certifications(certificationResponseDtos)
                .evaluations(evaluationResponseDtos)
                .build();
    }

    @Override
    @Transactional
    public ProfileResponseDto.CreateProfileResponseDto createProfile(ProfileRequestDto.CreateProfileRequestDto createProfileRequestDto, String username){
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));

        if(basicProfileRepository.findByMemberId(member.getId()).isPresent()){
            throw new ApplicationException(ApplicationErrorType.PROFILE_ALREADY_EXIST);
        }

        ProfileRequestDto.BasicProfileRequestDto basicProfileRequestDto = createProfileRequestDto.getBasicProfile();
        List<ProfileRequestDto.TechStackRequestDto> techStackRequestDtos = createProfileRequestDto.getTechStacks();
        storeUserGeneratedTechStackItem(techStackRequestDtos, member.getId());

        List<ProfileRequestDto.ActivityRequestDto> activityRequestDtos = createProfileRequestDto.getActivities();
        List<ProfileRequestDto.CertificationRequestDto> certificationRequestDtos = createProfileRequestDto.getCertifications();

        basicProfileRepository.save(basicProfileRequestDto.toEntity(member));
        techStackRepository.saveAll(techStackRequestDtos.stream().map(TechStackDto -> TechStackDto.toEntity(member)).collect(Collectors.toList()));
        activityRepository.saveAll(activityRequestDtos.stream().map(ActivityDto -> ActivityDto.toEntity(member)).collect(Collectors.toList()));
        certificationRepository.saveAll(certificationRequestDtos.stream().map(CertificationDto -> CertificationDto.toEntity(member)).collect(Collectors.toList()));

        return ProfileResponseDto.CreateProfileResponseDto.builder()
                .message("프로필 생성에 성공했습니다.")
                .build();
    }

    @Override
    @Transactional
    public ProfileResponseDto.UpdateProfileResponseDto updateProfile(ProfileRequestDto.UpdateProfileRequestDto updateProfileRequestDto, String username){
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));

        ProfileRequestDto.BasicProfileRequestDto basicProfileRequestDto = updateProfileRequestDto.getBasicProfile();
        List<ProfileRequestDto.TechStackRequestDto> techStackRequestDtos = updateProfileRequestDto.getTechStacks();
        storeUserGeneratedTechStackItem(techStackRequestDtos, member.getId());

        List<ProfileRequestDto.ActivityRequestDto> activityRequestDtos = updateProfileRequestDto.getActivities();
        List<ProfileRequestDto.CertificationRequestDto> certificationRequestDtos = updateProfileRequestDto.getCertifications();

        //BasicProfile
        if(basicProfileRequestDto.getCrudType()==CRUDType.DELETE){
            basicProfileRepository.deleteByMember(member);
        } else{
            basicProfileRepository.save(basicProfileRequestDto.toEntity(member));
        }

        // TechStacks
        List<ProfileRequestDto.TechStackRequestDto> deleteTechStacks = techStackRequestDtos.stream()
                .filter(dto -> dto.getCrudType() == CRUDType.DELETE)
                .collect(Collectors.toList());

        if (!deleteTechStacks.isEmpty()) {
            List<Long> techStackIdsToDelete = deleteTechStacks.stream()
                    .map(ProfileRequestDto.TechStackRequestDto::getId)
                    .collect(Collectors.toList());

            techStackRepository.deleteAllByIdInAndMember(techStackIdsToDelete, member);
        }

        List<TechStack> techStacks = techStackRequestDtos.stream()
                .filter(dto -> dto.getCrudType() != CRUDType.DELETE)
                .map(dto -> dto.toEntity(member))
                .collect(Collectors.toList());
        techStackRepository.saveAll(techStacks);

        // Activities
        List<ProfileRequestDto.ActivityRequestDto> deleteActivities = activityRequestDtos.stream()
                .filter(dto -> dto.getCrudType() == CRUDType.DELETE)
                .collect(Collectors.toList());

        if (!deleteActivities.isEmpty()) {
            List<Long> activityIdsToDelete = deleteActivities.stream()
                    .map(ProfileRequestDto.ActivityRequestDto::getId)
                    .collect(Collectors.toList());

            activityRepository.deleteAllByIdInAndMember(activityIdsToDelete, member);
        }

        List<Activity> activities = activityRequestDtos.stream()
                .filter(dto -> dto.getCrudType() != CRUDType.DELETE)
                .map(dto -> dto.toEntity(member))
                .collect(Collectors.toList());
        activityRepository.saveAll(activities);

        // Certifications
        List<ProfileRequestDto.CertificationRequestDto> deleteCertifications = certificationRequestDtos.stream()
                .filter(dto -> dto.getCrudType() == CRUDType.DELETE)
                .collect(Collectors.toList());

        if (!deleteCertifications.isEmpty()) {
            List<Long> certificationIdsToDelete = deleteCertifications.stream()
                    .map(ProfileRequestDto.CertificationRequestDto::getId)
                    .collect(Collectors.toList());

            certificationRepository.deleteAllByIdInAndMember(certificationIdsToDelete, member);
        }

        List<Certification> certifications = certificationRequestDtos.stream()
                .filter(dto -> dto.getCrudType() != CRUDType.DELETE)
                .map(dto -> dto.toEntity(member))
                .collect(Collectors.toList());
        certificationRepository.saveAll(certifications);


        return ProfileResponseDto.UpdateProfileResponseDto.builder()
                .message("프로필 수정에 성공했습니다.")
                .build();
    }

    public void storeUserGeneratedTechStackItem(List<ProfileRequestDto.TechStackRequestDto> techStackRequestDtos, Long memberId){
        //해당 멤버의 이미 있는 techStack들을 조회하여 techStackItem을 저장
        Optional<List<TechStack>> memberTechStacks = techStackRepository.findAllByMemberId(memberId);
        List<TechStackItem> memberTechStackItems = memberTechStacks
                .map(techStacks -> techStacks.stream().
                        map(TechStack::getTechStackItem)
                        .collect(Collectors.toList()))
                .orElse(List.of());

        // techStackRequestDtos에 있는 TechStackItem들 중에서 기존에 없는 것들만 필터링
        List<TechStackItem> newTechStackItems = techStackRequestDtos.stream()
                .map(ProfileRequestDto.TechStackRequestDto::getTechStackItem)
                .filter(techStackItem -> !memberTechStackItems.contains(techStackItem))
                .collect(Collectors.toList());

        techStackItemRepository.saveAll(newTechStackItems);
    }

    @Override
    @Transactional
    public void deleteProfile(String username){
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));

        try {
            basicProfileRepository.deleteByMember(member);
            techStackRepository.deleteAllByMember(member);
            activityRepository.deleteAllByMember(member);
            certificationRepository.deleteAllByMember(member);
        } catch(Exception e){
            throw new ApplicationException(ApplicationErrorType.PROFILE_ALREADY_EXIST);
        }
    }

    @Override
    @Transactional
    public ProfileResponseDto.CreateEvaluationResponseDto createEvaluation(ProfileRequestDto.CreateEvaluationRequestDto createEvaluationRequestDto, Long givenTeamId, Long subjectMemberId, String username) {
        Member authorMember = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Member subjectMember = memberRepository.findById(subjectMemberId).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Evaluation alreadyExistingEvaluation = evaluationRepository.findByAuthorMemberIdAndSubjectMemberId(authorMember.getId(), subjectMemberId).orElse(null);

        //이미 해당 사용자에 대한 평가 작성 완료함
        if (alreadyExistingEvaluation != null) {
            throw new ApplicationException(EVALUATION_ALREADY_EXIST);
        }

        //자가 자신 평가 불가능
        if (authorMember.equals(subjectMember)) {
            throw new ApplicationException(UNABLE_TO_EVALUATE_MYSELF);
        }

        //teamId에 해당하는 team 없을 경우
        Team givenTeam = teamRepository.findById(givenTeamId).orElseThrow(() -> new ApplicationException(TEAM_NOT_FOUND));

        //평가자와 피평가자가 주어진 팀에 동시에 속해있지 않을 경우
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdsAndTeamIds(authorMember.getId(), subjectMemberId, givenTeamId);
        if (memberTeam == null) throw new ApplicationException(USER_NOT_IN_TEAM);

        // 평가 점수 총합은 0~30 이어야 함
        Integer sum = createEvaluationRequestDto.getStat1() + createEvaluationRequestDto.getStat2() + createEvaluationRequestDto.getStat3() + createEvaluationRequestDto.getStat4() + createEvaluationRequestDto.getStat5();
        if (sum < 0 || sum > 30) throw new ApplicationException(EVALUATION_SCORE_NOT_IN_SCOPE);

        //save
        Evaluation evaluation = createEvaluationRequestDto.toEntity(authorMember, subjectMember, givenTeam);
        evaluationRepository.save(evaluation);

        return ProfileResponseDto.CreateEvaluationResponseDto.builder()
                .message("평가가 완료되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public ProfileResponseDto.UpdateEvaluationResponseDto updateEvaluation(ProfileRequestDto.UpdateEvaluationRequestDto updateEvaluationRequestDto, Long givenTeamId, Long subjectMemberId, String username) {
        Evaluation evaluationToUpdate = evaluationRepository.findById(updateEvaluationRequestDto.getId()).orElse(null);

        //updateEvaluationDto.getId()를 id로 갖는 평가가 없으면 예외
        if (evaluationToUpdate == null) {
            throw new ApplicationException(EVALUATION_NOT_EXIST);
        }

        //해당 평가의 작성자가 본인이 아니면 예외
        if (!evaluationToUpdate.getAuthorMember().getUsername().equals(username)) {
            throw new ApplicationException(NOT_AUTHOR_OF_EVALUATION);
        }

        Member authorMember = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Member subjectMember = memberRepository.findById(subjectMemberId).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        //자가 자신 평가 불가능
        if (authorMember.equals(subjectMember)) {
            throw new ApplicationException(UNABLE_TO_EVALUATE_MYSELF);
        }
        //teamId에 해당하는 team 없을 경우
        Team givenTeam = teamRepository.findById(givenTeamId).orElseThrow(() -> new ApplicationException(TEAM_NOT_FOUND));

        //평가자와 피평가자가 주어진 팀에 동시에 속해있지 않을 경우
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdsAndTeamIds(authorMember.getId(), subjectMemberId, givenTeamId);
        if (memberTeam == null) throw new ApplicationException(USER_NOT_IN_TEAM);

        // 평가 점수 총합은 0~30 이어야 함
        Integer sum = updateEvaluationRequestDto.getStat1() + updateEvaluationRequestDto.getStat2() + updateEvaluationRequestDto.getStat3() + updateEvaluationRequestDto.getStat4() + updateEvaluationRequestDto.getStat5();
        if (sum < 0 || sum > 30) throw new ApplicationException(EVALUATION_SCORE_NOT_IN_SCOPE);

        //save
        Evaluation evaluation = updateEvaluationRequestDto.toEntity(authorMember, subjectMember, givenTeam);
        evaluationRepository.save(evaluation);

        return ProfileResponseDto.UpdateEvaluationResponseDto.builder()
                .message("평가가 수정되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public void deleteEvaluation(ProfileRequestDto.DeleteEvaluationRequestDto deleteEvaluationRequestDto, Long givenTeamId, Long subjectMemberId, String username) {
        Long evaluationId = deleteEvaluationRequestDto.getId();

        //평가 존재 x
        Evaluation evaluationToDelete = evaluationRepository.findById(evaluationId).orElse(null);
        if (evaluationToDelete == null) {
            throw new ApplicationException(EVALUATION_NOT_EXIST);
        }

        Member authorMember = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Member subjectMember = memberRepository.findById(subjectMemberId).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        //자가 자신 평가 불가능
        if (authorMember.equals(subjectMember)) {
            throw new ApplicationException(UNABLE_TO_EVALUATE_MYSELF);
        }
        //teamId에 해당하는 team 없을 경우
        Team givenTeam = teamRepository.findById(givenTeamId).orElseThrow(() -> new ApplicationException(TEAM_NOT_FOUND));

        //평가자와 피평가자가 주어진 팀에 동시에 속해있지 않을 경우
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdsAndTeamIds(authorMember.getId(), subjectMemberId, givenTeamId);
        if (memberTeam == null) throw new ApplicationException(USER_NOT_IN_TEAM);


        evaluationRepository.deleteById(evaluationId);
    }

    @Override
    @Transactional
    public List<ProfileResponseDto.EvaluationResponseDto> getAllEvaluations(String username){
        Member authorMember = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        List<Evaluation> evaluations = evaluationRepository.findAllByAuthorMemberId(authorMember.getId()).orElseThrow(() -> new ApplicationException(EVALUATION_NOT_EXIST));
        List<ProfileResponseDto.EvaluationResponseDto> evaluationResponseDtos = evaluations.stream().map(Evaluation::toDto).collect(Collectors.toList());
        return evaluationResponseDtos;
      
    @Override
    @Transactional(readOnly = true)
    public BasicProfile getBasicProfile(String username) {

        return basicProfileRepository.findByMemberId(
                memberRepository.findMemberByUsername(username).orElseThrow(
                        () -> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND)
                ).getId()
        ).orElseThrow(
            () -> new ApplicationException(ApplicationErrorType.NOT_FOUND)
        );
    }
}
