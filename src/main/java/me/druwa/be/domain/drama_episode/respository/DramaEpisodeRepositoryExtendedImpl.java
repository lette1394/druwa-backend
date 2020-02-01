package me.druwa.be.domain.drama_episode.respository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.domain.drama_episode.model.DramaEpisodes;
import me.druwa.be.domain.drama_episode.model.QDramaEpisode;

public class DramaEpisodeRepositoryExtendedImpl
        extends QuerydslRepositorySupport
        implements DramaEpisodeRepositoryExtended {

    public DramaEpisodeRepositoryExtendedImpl() {
        super(DramaEpisode.class);
    }

    @Override
    public DramaEpisodes findByDramaId(final Long dramaId) {
        final QDramaEpisode episode = QDramaEpisode.dramaEpisode;

        return new DramaEpisodes(from(episode).where(episode.drama.dramaId.eq(dramaId))
                                              .fetch());
    }
}
