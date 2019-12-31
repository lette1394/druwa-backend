package me.druwa.be.domain.auth.service;

import java.util.Map;

import me.druwa.be.domain.auth.exception.OAuth2AuthenticationProcessingException;
import me.druwa.be.domain.auth.model.FacebookOAuth2UserInfo;
import me.druwa.be.domain.auth.model.KakaoOAuth2UserInfo;
import me.druwa.be.domain.auth.model.OAuth2UserInfo;
import me.druwa.be.domain.user.model.OAuth2Provider;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(OAuth2Provider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(OAuth2Provider.KAKAO.toString())) {
            return new KakaoOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}

