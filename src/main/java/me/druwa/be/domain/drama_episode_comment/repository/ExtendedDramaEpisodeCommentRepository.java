package me.druwa.be.domain.drama_episode_comment.repository;

import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComments;

public interface ExtendedDramaEpisodeCommentRepository {
    DramaEpisodeComments findByDramaEpisodeId(final Long episodeId);
}
