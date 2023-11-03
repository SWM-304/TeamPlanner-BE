package com.tbfp.teamplannerbe.domain.profile.service.impl;

import com.mysema.commons.lang.Pair;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.dto.MemberDto;
import com.tbfp.teamplannerbe.domain.profile.CRUDType;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileRequestDto;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.*;
import com.tbfp.teamplannerbe.domain.profile.entity.*;
import com.tbfp.teamplannerbe.domain.profile.repository.*;
import com.tbfp.teamplannerbe.domain.profile.service.ProfileService;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.MemberTeamRepository;
import com.tbfp.teamplannerbe.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
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

    private static final Map<String,Double> weightMap = new HashMap<>(){{
        put("job",3.0);
        put("education",3.0);
        put("admissionDate",6.0);
        put("birth",6.0);
        put("address",6.0);
        put("techStackItemIds",50.0);
        put("activitySubjects",10.0);
        put("certificationNames",8.0);
        put("averageStats",8.0);
    }};

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
        List<ProfileResponseDto.RecommendedUserResponseDto> recommendedUserResponseDtos = compareWithAllMembers(member.get());

        return ProfileResponseDto.ShowProfileResponseDto.builder()
                .basicProfile(basicProfileResponseDto)
                .techStacks(techStackResponseDtos)
                .activities(activityResponseDtos)
                .certifications(certificationResponseDtos)
                .evaluations(evaluationResponseDtos)
                .recommendedUsers(recommendedUserResponseDtos)
                .build();
    }

    public List<ProfileResponseDto.RecommendedUserResponseDto> compareWithAllMembers(Member member){
        //dto로 추출
        Long memberId = member.getId();

        //모든 사용자의 정보
        List<Member> members = memberRepository.findAll();

        List<MemberDto.ProfileInfoForScoringDto> profileInfoForScoringDtos = members.stream()
                .map(memberNow -> memberRepository.findProfileInfoForScoring(memberNow.getId()))
                .collect(Collectors.toList());

//        for(MemberDto.ProfileInfoForScoringDto profileInfoForScoringDto : profileInfoForScoringDtos){
//            System.out.println("시작 : "+ profileInfoForScoringDto.getId());
//            System.out.println("직업 : "+profileInfoForScoringDto.getJob());
//            System.out.println("교육 : "+profileInfoForScoringDto.getEducation());
//            System.out.println("입학 : "+profileInfoForScoringDto.getAdmissionDate());
//            System.out.println("생일 : "+profileInfoForScoringDto.getBirth());
//            System.out.println("주소 : "+profileInfoForScoringDto.getAddress());
//            System.out.println("기술스택");
//            for(MemberDto.ProfileInfoForScoringDto.TechStackItemDto techStackItemDto: profileInfoForScoringDto.getTechStackItems()){
//                System.out.println(techStackItemDto.getName());
//            }
//            System.out.println("활동");
//            for(String activitySubject : profileInfoForScoringDto.getActivitySubjects()){
//                System.out.println(activitySubject);
//            }
//            System.out.println("자격증 이름");
//            for(String certificationName : profileInfoForScoringDto.getCertificationNames()){
//                System.out.println(certificationName);
//            }
//        }
        //현재 사용자의 정보
        MemberDto.ProfileInfoForScoringDto myInfoForScoringDto = profileInfoForScoringDtos.stream()
                .filter(dto -> dto.getId().equals(memberId))
                .findFirst()
                .orElse(null);

        //모든 사용자 - 현재 사용자
        profileInfoForScoringDtos.removeIf(dto -> dto.getId().equals(memberId));

        //1대1로 비교해서 score, similarities 매기기
        List<MemberDto.ScoreAndSimilaritiesDto> scoreAndSimilaritiesList = new ArrayList<>();

        for(MemberDto.ProfileInfoForScoringDto otherInfoForScoringDto : profileInfoForScoringDtos){
            MemberDto.ScoreAndSimilaritiesDto scoreAndSimilarities = getMatchingScore(myInfoForScoringDto,otherInfoForScoringDto);
            scoreAndSimilaritiesList.add(scoreAndSimilarities);
        }
        //scoreAndSimilaritiesList 정렬해서 상위 5개에 대해 dto build하고 list로 리턴
        //정렬
        return scoreAndSimilaritiesList.stream()
                .sorted(Comparator.comparing(MemberDto.ScoreAndSimilaritiesDto::getScore).reversed())
                .limit(5)
                .map(entry -> memberRepository.getRecommendedUserResponseDto(entry.getId(),entry.getSimilarities()))
                .collect(Collectors.toList());
    }
    public MemberDto.ScoreAndSimilaritiesDto getMatchingScore(MemberDto.ProfileInfoForScoringDto memberInfo1, MemberDto.ProfileInfoForScoringDto memberInfo2){
        Double score = 0.0;
        Double tempScore = 0.0;
        List<Pair<String, Double>> weightedSimilarities = new ArrayList<>();

        //똑같은거 있으면 socre+1
        // job
        if (!containsNull(memberInfo1.getJob(), memberInfo2.getJob()) && memberInfo1.getJob() == memberInfo2.getJob()){
            tempScore = weightMap.get("job");
            weightedSimilarities.add(new Pair<>(memberInfo1.getJob().getLabel(), tempScore));
        }

        // education
        if (!containsNull(memberInfo1.getEducation(), memberInfo2.getEducation()) && memberInfo1.getEducation() == memberInfo2.getEducation()) {
            tempScore = weightMap.get("education");
            weightedSimilarities.add(new Pair<>(memberInfo1.getEducation().getLabel(), tempScore));
        }

        // admissionDate : 차이가 3년 이하면 가중치 따라 score 부여
        if(!containsNull(memberInfo1.getAdmissionDate(), memberInfo2.getAdmissionDate())){
            Integer admissionDateDiff = Period.between(memberInfo1.getAdmissionDate(),memberInfo2.getAdmissionDate()).getYears();
            if (Math.abs(admissionDateDiff) < 3){
                tempScore = (weightMap.get("admissionDate")/3) * (3-admissionDateDiff);
                weightedSimilarities.add(new Pair<>("입학년도", tempScore));
            }
        }

        //birth
        if(!containsNull(memberInfo1.getBirth(), memberInfo2.getBirth())){
            Integer birthDiff = Math.abs(memberInfo1.getBirth().getYear()-memberInfo2.getBirth().getYear());
            if (birthDiff < 3){
                tempScore = (weightMap.get("birth") / 3) * (3-birthDiff);
                weightedSimilarities.add(new Pair<>("나이", tempScore));
            }
        }

        // address
        if(!containsNull(memberInfo1.getAddress(), memberInfo2.getAddress())) {
            tempScore = weightMap.get("address") * getMatchWordCount(memberInfo1.getAddress(), memberInfo2.getAddress());
            weightedSimilarities.add(new Pair<>("거주지", tempScore));
        }

        //techStackItemIds
        if(!containsNull(memberInfo1.getTechStackItems(), memberInfo2.getTechStackItems())) {
            // 0. techStackItems 추출
            List<MemberDto.ProfileInfoForScoringDto.TechStackItemDto> techStackItems1 = memberInfo1.getTechStackItems();
            List<MemberDto.ProfileInfoForScoringDto.TechStackItemDto> techStackItems2 = memberInfo2.getTechStackItems();

            // 1. id 같으면 skillLevel 비교해서 techStackSimilarities에 넣기
            List<Pair<String, Double>> techStackSimilarities = new ArrayList<>();
            for (MemberDto.ProfileInfoForScoringDto.TechStackItemDto item1 : techStackItems1) {
                for (MemberDto.ProfileInfoForScoringDto.TechStackItemDto item2 : techStackItems2) {
//                System.out.println("item1 : " + item1.getId()+" "+ item1.getName() + " " + item1.getSkillLevel());
                    if (item1.getId().equals(item2.getId())) {
                        // id가 같은 경우 skillLevel 비교
                        double skillLevelSimilarity = 9.0;
                        skillLevelSimilarity += ((1.0 / 2) * (2 - Math.abs(item1.getSkillLevel() - item2.getSkillLevel())));
                        //기술스택 이름, 점수 넣기
                        techStackSimilarities.add(new Pair<>(item1.getName(), skillLevelSimilarity));
                    }
                }
            }

//        System.out.println("techStackSimilarities : ");
//        for(Pair<String,Double> p: techStackSimilarities){
//            System.out.println(p.getFirst() + " " + p.getSecond());
//        }
            // 2. sort해서 상위 5개 weightedSimilarities에 넣기
            techStackSimilarities.sort((pair1, pair2) -> Double.compare(pair2.getSecond(), pair1.getSecond()));
            for (int i = 0; i < Math.min(5, techStackSimilarities.size()); i++) {
                weightedSimilarities.add(techStackSimilarities.get(i));
            }
        }

        if(!containsNull(memberInfo1.getActivitySubjects(), memberInfo2.getActivitySubjects())) {
            // activitySubjects
            tempScore = (weightMap.get("activitySubjects") / 3) * Math.min(3, Math.abs(memberInfo1.getActivitySubjects().size() - memberInfo2.getActivitySubjects().size()));
            weightedSimilarities.add(new Pair<>("완료활동", tempScore));
        }

        // certificationNames
        if(!containsNull(memberInfo1.getCertificationNames(), memberInfo2.getCertificationNames())) {
            //        System.out.println("certificationNames : " + memberInfo2.getCertificationNames().get(0));
            tempScore = (weightMap.get("certificationNames") / 3) * Math.min(3, Math.abs(memberInfo1.getCertificationNames().size() - memberInfo2.getCertificationNames().size()));
            weightedSimilarities.add(new Pair<>("자격증/수상이력", tempScore));
        }

        if(!containsNull(memberInfo1.getAverageStat(), memberInfo2.getAverageStat())){
            //averageStats
            if (memberInfo1.getAverageStat()!=0.0 && memberInfo2.getAverageStat()!=0.0){
                tempScore = (weightMap.get("averageStats") / 5) * Math.min(5,5 - Math.abs(memberInfo1.getAverageStat() - memberInfo2.getAverageStat()));
                weightedSimilarities.add(new Pair<>("팀원평가", tempScore));
            }
        }

        List<String> similarities = new ArrayList<>();
        weightedSimilarities.sort((pair1, pair2) -> Double.compare(pair2.getSecond(), pair1.getSecond()));
        //weightedSimilarities 순회하여 score += getSecond, 상위 3개 similarities에 추가
        for (Pair<String,Double> pair : weightedSimilarities){
            score += pair.getSecond();
            if(similarities.size() < 3){
                similarities.add(pair.getFirst());
            }
        }

//        System.out.println(memberInfo2.getId()+"점수"+score);
//        for(Pair<String,Double> p: weightedSimilarities){
//            System.out.println(p.getFirst() + " " + p.getSecond());
//        }
//        System.out.println(similarities);

        return MemberDto.ScoreAndSimilaritiesDto.builder()
                .id(memberInfo2.getId())
                .score(score)
                .similarities(similarities)
                .build();
    }

    public boolean containsNull(Object item1, Object item2){
        if (item1==null || item2==null){
            return true;
        }
        return false;
    }

    public double getMatchWordCount(String addressStr1, String addressStr2){
        Integer levenshteinDistance = StringUtils.getLevenshteinDistance(addressStr1,addressStr2);
        double maxLength = Double.max(addressStr1.length(), addressStr2.length());
        double percentage = (maxLength - levenshteinDistance) / maxLength;
        return percentage;
    }


    @Override
    @Transactional
    public ProfileResponseDto.GetProfileResponseDto getProfile(String username){

        Optional<Member> member = memberRepository.findMemberByUsername(username);
        if(!member.isPresent()) throw new ApplicationException(ApplicationErrorType.USER_NOT_FOUND);

        Long memberId = member.get().getId();
        String nickname = member.get().getNickname();

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
                .nickname(nickname)
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

        // 평가 점수 총합은 0~40 이어야 함
        Integer sum = updateEvaluationRequestDto.getStat1() + updateEvaluationRequestDto.getStat2() + updateEvaluationRequestDto.getStat3() + updateEvaluationRequestDto.getStat4() + updateEvaluationRequestDto.getStat5();
        if (sum < 0 || sum > 40) throw new ApplicationException(EVALUATION_SCORE_NOT_IN_SCOPE);

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
    public List<ProfileResponseDto.EvaluationResponseDto> getAllEvaluations(String username) {
        Member authorMember = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        List<Evaluation> evaluations = evaluationRepository.findAllByAuthorMemberId(authorMember.getId()).orElseThrow(() -> new ApplicationException(EVALUATION_NOT_EXIST));
        List<ProfileResponseDto.EvaluationResponseDto> evaluationResponseDtos = evaluations.stream().map(Evaluation::toDto).collect(Collectors.toList());
        return evaluationResponseDtos;
    }

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

    @Override
    @Transactional
    public ProfileResponseDto.GetTechStackItemsResponseDto getTechStackItems(){
        List<TechStackItem> techStackItems = techStackItemRepository.findByUserGeneratedFalse();

        return ProfileResponseDto.GetTechStackItemsResponseDto.builder()
                .techStackItems(techStackItems)
                .build();
    }
}
