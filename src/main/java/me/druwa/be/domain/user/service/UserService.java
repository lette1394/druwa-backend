package me.druwa.be.domain.user.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.auth.model.OAuth2UserInfo;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.user.model.Authorities;
import me.druwa.be.domain.user.model.OAuth2Provider;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCacheService userCacheService;

    @Cacheable(cacheNames = CacheKey.User.EMAIL, key = "#email")
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User create(final OAuth2UserRequest oAuth2UserRequest, final OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(OAuth2Provider.parse(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setAuthorities(Authorities.user());
        return userRepository.save(user);
    }

    public User update(final User user) {
        userCacheService.evictAll(user);
        return userRepository.save(user);
    }
}
