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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


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
}
