package me.druwa.be.domain.drama.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.drama.model.Drama;

@Repository
public interface DramaRepository extends JpaRepository<Drama, Long>,
                                         ExtendedDramaRepository {
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheKey.Drama.EXISTS, key = "#id")
    boolean existsByDramaId(final long id);
}
