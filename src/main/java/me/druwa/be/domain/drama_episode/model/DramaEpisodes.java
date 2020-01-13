package me.druwa.be.domain.drama_episode.model;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DramaEpisodes {

    @OneToMany(mappedBy = "drama")
    private List<DramaEpisode> dramaEpisodes;

    public static DramaEpisodes dramaEpisodes(final List<DramaEpisode> dramaEpisodes) {
        return new DramaEpisodes(dramaEpisodes);
    }

    public void update(final DramaEpisodes other) {
        this.dramaEpisodes = other.dramaEpisodes;
    }
}
