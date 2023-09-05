package com.tbfp.teamplannerbe.domain.chat.dto.response;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Long memberProfileId;
    private String nickname;
    private String image;

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getNickname(), member.getBasicProfile().getProfileImage());
    }
}
