package me.druwa.be.domain.drama_episode.model;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.db.JoinTableName;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComments;

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

    @Embedded
    @AssociationOverride(name = "dramaEpisodeComments",
                         joinTable = @JoinTable(name = JoinTableName.DRAMA_EPISODE__HAS__DRAMA_EPISODE_COMMENT,
                                                joinColumns = @JoinColumn(name = "drama_id"),
                                                inverseJoinColumns = @JoinColumn(name = "drama_episode_comment_id")))
    private DramaEpisodeComments dramaEpisodeComments;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong number;
}
