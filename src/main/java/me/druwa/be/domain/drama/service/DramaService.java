package me.druwa.be.domain.drama.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.common.service.S3Service;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.model.DramaImageRepository;
import me.druwa.be.domain.drama.model.DramaImages;
import me.druwa.be.domain.drama.model.DramaMultipartImages;
import me.druwa.be.domain.drama.model.LikeOrDislike;
import me.druwa.be.domain.drama.repository.DramaRepository;
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

    private final DramaImageRepository dramaImageRepository;
    private final S3Service s3Service;

//    @Cacheable(cacheNames = CacheKey.Drama.ID, key = "#dramaId")
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

    @Transactional
    public Drama update(final Long dramaId, final DramaTags dramaTags) {
        final DramaTags savedDramaTags = dramaTagService.createIfNotExists(dramaTags);
        return findByDramaId(dramaId).update(savedDramaTags);
    }

    @Transactional
    public LikeOrDislike doLike(final Long dramaId, final User user) {
        return findByDramaId(dramaId).like(user);
    }

    @Transactional
    public LikeOrDislike doDislike(final Long dramaId, final User user) {
        return findByDramaId(dramaId).dislike(user);
    }

    @Transactional
    public Drama update(final Drama dramaBefore,
                        final User user,
                        final Drama.View.Patch.Request body) {
        final Drama drama = dramaBefore.merge(body.toPartialDrama());
        dramaCacheService.evictByDramaId(dramaBefore.getDramaId());

        return drama;
    }

    @Transactional
    public Drama createDramaImage(final long dramaId, final DramaMultipartImages images) {
        final Drama drama = findByDramaId(dramaId);
        final DramaImages s3SavedImages = s3Service.put(images)
                                                   .toDramaImages(drama);
        dramaImageRepository.saveAll(s3SavedImages);

        return drama.merge(s3SavedImages);
    }
}
