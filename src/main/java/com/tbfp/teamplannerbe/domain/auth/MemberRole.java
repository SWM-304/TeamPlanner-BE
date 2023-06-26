package com.tbfp.teamplannerbe.domain.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    MEMBER("ROLE_MEMBER"),GUEST("ROLE_GUEST"),ADMIN("ROLE_ADMIN");
    private final String key;
}
