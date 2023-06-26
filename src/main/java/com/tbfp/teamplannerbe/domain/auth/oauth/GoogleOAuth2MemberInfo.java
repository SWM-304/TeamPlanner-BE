package com.tbfp.teamplannerbe.domain.auth.oauth;

import java.util.Map;

public class GoogleOAuth2MemberInfo extends OAuth2MemberInfo {
    public GoogleOAuth2MemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
