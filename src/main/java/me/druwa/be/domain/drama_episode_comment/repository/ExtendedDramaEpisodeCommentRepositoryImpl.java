package me.druwa.be.domain.drama_episode_comment.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;

class ExtendedDramaEpisodeCommentRepositoryImpl extends QuerydslRepositorySupport
        implements ExtendedDramaEpisodeCommentRepository {

    public ExtendedDramaEpisodeCommentRepositoryImpl() {
        super(DramaEpisodeComment.class);
    }
}
