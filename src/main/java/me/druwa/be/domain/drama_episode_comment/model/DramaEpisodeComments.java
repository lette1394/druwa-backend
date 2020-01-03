package me.druwa.be.domain.drama_episode_comment.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DramaEpisodeComments {
    @Column
    @NotNull
    @OneToMany
    private List<DramaEpisodeComment> dramaEpisodeComments;
}
