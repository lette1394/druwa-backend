package me.druwa.be.domain.user.service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Profile("test")
@Primary
public class TestUserService implements UserService {
    private final UserRepository userRepository;
    private final UserCacheService userCacheService;

    @Cacheable(cacheNames = CacheKey.User.EMAIL, key = "#email")
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Cacheable(cacheNames = CacheKey.User.NAME, key = "#name")
    public Optional<User> findByName(final String name) {
        return userRepository.findByName(name);
    }

    public User save(final User user) {
        user.setUserId(0L);
        return user;
    }

    public User create(final OAuth2UserRequest oAuth2UserRequest, final OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setUserId(0L);
        user.setProvider(OAuth2Provider.parse(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setAuthorities(Authorities.user());
        return user;
    }

    public User update(final User user) {
        userCacheService.evictAll(user);
        return userRepository.save(user);
    }

    public void sendVerifiedEmail(final User user) {

    }

    @Transactional
    public User markVerifiedUser(final Long userIdFromToken) {
        User user = userRepository.findById(userIdFromToken)
                                  .orElseThrow(NoSuchElementException::new);

        user.setEmailVerified(true);

        log.info("user email verified. user:{}", user);
        return user;
    }

    public void ensureExist(final User user) {
        if (Objects.nonNull(user.getUserId()) && userRepository.existsById(user.getUserId()) == false) {
            throw new NoSuchElementException(String.format("No such user: %s", user));
        } else if (Objects.nonNull(user.getEmail()) && userRepository.existsByEmail(user.getEmail()) == false) {
            throw new NoSuchElementException(String.format("No such user: %s", user));
        }
    }

    public boolean isExistedByEmail(final User user) {
        return false;
    }
}
