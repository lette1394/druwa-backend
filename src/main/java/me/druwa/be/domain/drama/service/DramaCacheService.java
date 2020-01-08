package me.druwa.be.domain.drama.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import me.druwa.be.domain.common.cache.CacheKey;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Service
public class DramaCacheService {

    @CacheEvict(cacheNames = CacheKey.Drama.ID, key = "#dramaId")
    public void evictByDramaId(final Long dramaId) {
    }
}
