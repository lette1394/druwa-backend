package me.druwa.be.domain.post.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import me.druwa.be.domain.episode.model.EpisodeComment;

@Entity
@Table(name = "post_")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToMany
    private List<EpisodeComment> episodeComments = new ArrayList<>();

    public Optional<EpisodeComment> getLastComment() {
        if (episodeComments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(episodeComments.get(episodeComments.size() - 1));
    }
}
