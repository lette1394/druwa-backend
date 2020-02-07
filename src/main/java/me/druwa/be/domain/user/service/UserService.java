package me.druwa.be.domain.user.service;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import me.druwa.be.domain.auth.model.OAuth2UserInfo;
import me.druwa.be.domain.user.model.User;

@Service
public interface UserService {
    Optional<User> findByEmail(final String email);

    Optional<User> findByName(final String name);

    User save(final User user);

    User create(final OAuth2UserRequest oAuth2UserRequest, final OAuth2UserInfo oAuth2UserInfo);

    User update(final User user);

    void sendVerifiedEmail(final User user);

    User markVerifiedUser(final Long userIdFromToken);

    void ensureExist(final User user);

    boolean isExistedByEmail(final User user);
}
