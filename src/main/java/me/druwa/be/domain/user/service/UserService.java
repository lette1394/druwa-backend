package me.druwa.be.domain.user.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.auth.model.OAuth2UserInfo;
import me.druwa.be.domain.auth.service.TokenProvider;
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
    private final EmailService emailService;
    private final TokenProvider tokenProvider;

    @Cacheable(cacheNames = CacheKey.User.EMAIL, key = "#email")
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Cacheable(cacheNames = CacheKey.User.NAME, key = "#name")
    public Optional<User> findByName(final String name) {
        return userRepository.findByName(name);
    }

    public User save(final User user) {
        return userRepository.save(user);
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

    public void sendVerifiedEmail(final User user) {
        final String validationUrl = String.format("https://api.druwa.site/users/validation?token=%s",
                                                   tokenProvider.createToken(user));
        final String text = String.format("D.Studio 계정 확인 email 입니다. 아래 링크를 눌러주세요.\n%s", validationUrl);
        final String subject = "D.Studio email validation";

        emailService.sendBuilder()
                    .to(user.getEmail())
                    .text(text)
                    .subject(subject)
                    .send();
    }

    @Transactional
    public User markVerifiedUser(final Long userIdFromToken) {
        User user = userRepository.findById(userIdFromToken)
                                  .orElseThrow(NoSuchElementException::new);

        user.setEmailVerified(true);

        log.info("user email verified. user:{}", user);
        return user;
    }
}
