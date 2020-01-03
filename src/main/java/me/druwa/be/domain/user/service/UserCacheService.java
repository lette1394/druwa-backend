package me.druwa.be.domain.user.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.user.model.User;

@Slf4j
@Service
public class UserCacheService {

    public void evictAll(final User user) {
        evictById(user);
        evictByEmail(user);
    }

    @CacheEvict(cacheNames = CacheKey.User.ID, key = "#user.userId")
    public void evictById(final User user) {
        log.info(String.format("user cache is evicted by id, userId: [%s]", user.getUserId()));
    }

    @CacheEvict(cacheNames = CacheKey.User.EMAIL, key = "#user.email")
    public void evictByEmail(final User user) {
        log.info(String.format("user cache is evicted by email, userEmail: [%s]", user.getEmail()));
    }
}
