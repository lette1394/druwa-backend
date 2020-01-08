package me.druwa.be.domain.drama_episode.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;

@Entity
@EqualsAndHashCode(of = "dramaEpisodeId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drama_episode_")
@Builder
public class DramaEpisode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long dramaEpisodeId;

    @Column
    private String title;

    @OneToMany
    private Set<DramaEpisodeComment> dramaEpisodeComments;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong number;
}
