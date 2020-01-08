package me.druwa.be.domain.drama.service;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.repository.DramaRepository;
import me.druwa.be.domain.drama_episode_comment.model.Like;
import me.druwa.be.domain.user.model.User;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DramaService {
    private final DramaRepository repository;
    private final DramaCacheService dramaCacheService;

    @Cacheable(cacheNames = CacheKey.Drama.ID, key = "#dramaId")
    public Drama findByDramaId(final long dramaId) {
        return repository.findById(dramaId)
                         .orElseThrow(() -> new NoSuchElementException(format("no drama with id:[%s]", dramaId)));
    }

    public void ensureExistsBy(final long id) {
        if (repository.existsByDramaId(id)) {
            return;
        }
        throw new NoSuchElementException(format("no drama with id: [%s]", id));
    }

    public Drama create(final User user, final Drama.View.Create.Request body) {
        final Drama drama = Drama.builder()
                                 .title(body.getTitle())
                                 .productionCompany(body.getProductionCompany())
                                 .summary(body.getSummary())
                                 .registeredBy(user)
                                 .build();

        return repository.save(drama);
    }

    @Transactional
    public Like doLike(final User user, @Valid final long dramaId) {
        return findByDramaId(dramaId).doLike(user);
    }

    @Transactional
    public Like doDislike(final User user, final long dramaId) {
        return findByDramaId(dramaId).doDislike(user);
    }

    @Transactional
    public Drama update(final Drama dramaBefore,
                        final User user,
                        final Drama.View.Patch.Request body) {
        final Drama drama = dramaBefore.merge(body.toPartialDrama());
        dramaCacheService.evictByDramaId(dramaBefore.getDramaId());

        return drama;
    }
}
