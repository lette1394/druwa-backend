package me.druwa.be.domain.episode.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.druwa.be.domain.episode.model.Episode;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
}
