package me.druwa.be.domain.episode.respository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import me.druwa.be.domain.episode.model.EpisodeComment;

class ExtendedEpisodeCommentRepositoryImpl extends QuerydslRepositorySupport
        implements ExtendedEpisodeCommentRepository {

    public ExtendedEpisodeCommentRepositoryImpl() {
        super(EpisodeComment.class);
    }
}
