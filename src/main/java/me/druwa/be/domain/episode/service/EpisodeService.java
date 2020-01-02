package me.druwa.be.domain.episode.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.episode.respository.EpisodeRepository;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository repository;


    public void ensureExistsBy(final long episodeId) {
        if (repository.existsById(episodeId)) {
            return;
        }
        throw new NoSuchElementException(format("no episode with id:[%s]", episodeId));
    }
}
