package me.druwa.be.domain.drama_episode_comment.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComments;
import me.druwa.be.domain.drama_episode_comment.model.QDramaEpisodeComment;

class ExtendedDramaEpisodeCommentRepositoryImpl extends QuerydslRepositorySupport
        implements ExtendedDramaEpisodeCommentRepository {

    public ExtendedDramaEpisodeCommentRepositoryImpl() {
        super(DramaEpisodeComment.class);
    }

    @Override
    public DramaEpisodeComments findByDramaEpisodeId(final Long dramaEpisodeId) {
        final QDramaEpisodeComment comment = QDramaEpisodeComment.dramaEpisodeComment;

        return new DramaEpisodeComments(
                from(comment)
                        .where(comment.dramaEpisode.dramaEpisodeId.eq(dramaEpisodeId))
                        .fetch());
    }
}
