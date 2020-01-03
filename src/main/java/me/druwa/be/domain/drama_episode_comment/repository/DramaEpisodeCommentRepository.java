package me.druwa.be.domain.drama_episode_comment.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;

@Repository
public interface DramaEpisodeCommentRepository extends JpaRepository<DramaEpisodeComment, Long>,
                                                       ExtendedDramaEpisodeCommentRepository {
    @Cacheable(cacheNames = CacheKey.DramaEpisodeComment.EXISTS, key = "#id")
    @Transactional(readOnly = true)
    boolean existsByDramaEpisodeCommentId(final long id);
}
