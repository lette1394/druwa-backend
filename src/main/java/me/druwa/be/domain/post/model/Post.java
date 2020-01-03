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

import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;

@Entity
@Table(name = "post_")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @OneToMany
    private List<DramaEpisodeComment> dramaEpisodeComments = new ArrayList<>();

    public Optional<DramaEpisodeComment> getLastComment() {
        if (dramaEpisodeComments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(dramaEpisodeComments.get(dramaEpisodeComments.size() - 1));
    }
}
