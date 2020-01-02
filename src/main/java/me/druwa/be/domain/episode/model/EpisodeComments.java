package me.druwa.be.domain.episode.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeComments {
    @Column
    @NotNull
    @OneToMany
    private List<EpisodeComment> episodeComments;
}
