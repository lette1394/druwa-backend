package me.druwa.be.domain.drama_episode.respository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;

@Repository
public interface DramaEpisodeRepository extends JpaRepository<DramaEpisode, Long> {

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheKey.DramaEpisode.EXISTS, key = "#id")
    boolean existsByDramaEpisodeId(final long id);
}
