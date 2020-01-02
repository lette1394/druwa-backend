package me.druwa.be.domain.episode.model;

import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drama_episode_")
@Builder
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Embedded
    private EpisodeComments episodeComments;

    @Column
    @Convert(converter = PositiveLongConverter.class)
    private PositiveOrZeroLong number;
}
