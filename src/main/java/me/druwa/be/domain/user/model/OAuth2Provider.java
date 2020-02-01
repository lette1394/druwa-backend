package me.druwa.be.domain.user.model;

import org.apache.commons.lang3.StringUtils;

public enum OAuth2Provider {
    KAKAO, FACEBOOK, LOCAL;

    public static OAuth2Provider parse(String provider) {
        return OAuth2Provider.valueOf(StringUtils.upperCase(provider));
    }
}
