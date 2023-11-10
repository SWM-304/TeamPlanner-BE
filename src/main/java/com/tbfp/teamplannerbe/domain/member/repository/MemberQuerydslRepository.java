package com.tbfp.teamplannerbe.domain.member.repository;

import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.member.dto.MemberDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberQuerydslRepository {
    List<Member> basicSelect();

    /**
     *
     * username username가 null값이 들어오면 오류뜸 예외처리 필요
     *
     */
    Optional<Member> findMemberByUsername(String username);

    Optional<Member> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);

    void updateMemberStateFalseByUsername(String username);

    Optional<List<String>> findUsernamesByEmail(String email);

    void updateMemberPassword(Member theMember, String password);

    List<Recruitment> getApplicantList(String username);

    MemberDto.ProfileInfoForScoringDto findProfileInfoForScoring(Long memberId);


    ProfileResponseDto.RecommendedUserResponseDto getRecommendedUserResponseDto(Long id, List<String> similarities);

    void updateNickname(String username, String nickname);
}
