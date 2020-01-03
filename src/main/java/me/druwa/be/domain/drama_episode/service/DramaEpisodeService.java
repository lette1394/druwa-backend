package me.druwa.be.domain.drama_episode.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.drama_episode.respository.DramaEpisodeRepository;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DramaEpisodeService {
    private final DramaEpisodeRepository repository;

    public void ensureExistsBy(final long episodeId) {
        if (repository.existsByDramaEpisodeId(episodeId)) {
            return;
        }
        throw new NoSuchElementException(format("no episode with id:[%s]", episodeId));
    }
}
