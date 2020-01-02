package me.druwa.be.domain.episode.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.druwa.be.domain.episode.model.EpisodeComment;

@Repository
public interface EpisodeCommentRepository extends JpaRepository<EpisodeComment, Long>,
                                                  ExtendedEpisodeCommentRepository {
}
