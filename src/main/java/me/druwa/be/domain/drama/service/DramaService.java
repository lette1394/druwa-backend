package me.druwa.be.domain.drama.service;

import java.util.NoSuchElementException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.common.service.S3Service;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.repository.DramaRepository;
import me.druwa.be.domain.drama_episode_comment.model.Like;
import me.druwa.be.domain.drama_tag.DramaTagService;
import me.druwa.be.domain.drama_tag.DramaTags;
import me.druwa.be.domain.user.model.User;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DramaService {
    private final DramaRepository repository;
    private final DramaCacheService dramaCacheService;
    private final DramaTagService dramaTagService;

    private final S3Service s3Service;

    @Cacheable(cacheNames = CacheKey.Drama.ID, key = "#dramaId")
    public Drama findByDramaId(final Long dramaId) {
        return repository.findById(dramaId)
                         .orElseThrow(() -> new NoSuchElementException(format("no drama with id:[%s]", dramaId)));
    }

    public void ensureExistsBy(final Long dramaId) {
        if (repository.existsByDramaId(dramaId)) {
            return;
        }
        throw new NoSuchElementException(format("no drama with id: [%s]", dramaId));
    }

    public Drama create(final Drama drama) {
        return repository.save(drama);
    }

    public Drama create(final User user, final Drama.View.Create.Request body) {
        final Drama drama = body.toPartialDrama()
                                .populateUser(user);
        return repository.save(drama);
    }

    @SneakyThrows
    public Drama createMultipart(final User user, final Drama.View.Create.MultipartRequest body) {
        final String key = s3Service.put(body.getImage());

        final Drama drama = body.toPartialDrama()
                                .populateUser(user)
                                .populateImageKey(key);

        return create(drama);
    }

    @Transactional
    public Drama update(final Long dramaId, final DramaTags dramaTags) {
        final DramaTags savedDramaTags = dramaTagService.create(dramaTags);
        return findByDramaId(dramaId).update(savedDramaTags);
    }

    @Transactional
    public Like doLike(final Long dramaId, final User user) {
        return findByDramaId(dramaId).doLike(user);
    }

    @Transactional
    public Like doDislike(final Long dramaId, final User user) {
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
