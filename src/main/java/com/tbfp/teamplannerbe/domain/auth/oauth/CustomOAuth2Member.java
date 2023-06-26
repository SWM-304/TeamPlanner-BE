package com.tbfp.teamplannerbe.domain.auth.oauth;

import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2Member extends DefaultOAuth2User {
    private String loginId;
    private MemberRole memberRole;


    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2Member(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String loginId, MemberRole memberRole) {
        super(authorities, attributes, nameAttributeKey);
        this.loginId = loginId;
        this.memberRole = memberRole;
    }
}
