package me.druwa.be.domain.drama_episode.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.domain.drama_episode.model.DramaEpisodes;
import me.druwa.be.domain.drama_episode.respository.DramaEpisodeRepository;
import me.druwa.be.domain.user.model.User;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DramaEpisodeService {
    private final DramaEpisodeRepository repository;

    public void ensureExistsBy(final Long episodeId) {
        if (repository.existsByDramaEpisodeId(episodeId)) {
            return;
        }
        throw new NoSuchElementException(format("no episode with id:[%s]", episodeId));
    }

    public DramaEpisode findByEpisodeId(final Long episodeId) {
        return repository.findById(episodeId)
                         .orElseThrow(NoSuchElementException::new);
    }

    public DramaEpisodes findByDramaId(final Long dramaId) {
        return repository.findByDramaId(dramaId);
    }

    @Transactional
    public DramaEpisode create(final User user,
                               final Drama drama,
                               final DramaEpisode.View.Create.Request body) {
        final DramaEpisode dramaEpisode = body.toPartialDramaEpisode()
                                              .drama(drama)
                                              .user(user)
                                              .build();
        return repository.save(dramaEpisode);
    }
}
