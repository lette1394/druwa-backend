package me.druwa.be.domain.drama_episode.respository;

import me.druwa.be.domain.drama_episode.model.DramaEpisodes;

public interface DramaEpisodeRepositoryExtended  {
    DramaEpisodes findByDramaId(final Long dramaId);
}
