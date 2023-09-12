package com.tbfp.teamplannerbe.domain.auth.oauth;

import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Getter
@Slf4j
public class OAuthAttributes {
    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2MemberInfo oauth2MemberInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2MemberInfo oauth2MemberInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2MemberInfo = oauth2MemberInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(ProviderType providerType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (providerType == ProviderType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        if(providerType==ProviderType.NAVER){
            return ofNaver(userNameAttributeName,attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2MemberInfo(new KakaoOAuth2MemberInfo(attributes))
                .build();
    }
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2MemberInfo(new NaverOAuth2MemberInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2MemberInfo(new GoogleOAuth2MemberInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public Member toEntity(ProviderType providerType, OAuth2MemberInfo oauth2MemberInfo) {
        log.info("providerType.name() + \"-\" + UUID.randomUUID() = " + providerType.name() + "-" + UUID.randomUUID());
        return Member.builder()
                .providerType(providerType)
                .providerId(oauth2MemberInfo.getId())
                .email(oauth2MemberInfo.getEmail()) //
                .username(providerType.name() + "-" + UUID.randomUUID())
                .state(true)
                .password("" + UUID.randomUUID())
                .nickname(providerType.name() + "-" + UUID.randomUUID())
//                .nickname(oauth2MemberInfo.getNickname())
//                .imageUrl(oauth2MemberInfo.getImageUrl())
                .memberRole(MemberRole.MEMBER)
                .build();
    }
}
