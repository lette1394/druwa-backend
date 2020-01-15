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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.db.JoinTableName;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComments;
import me.druwa.be.domain.drama_episode_comment.model.Like;

@Entity
@Table(name = "drama_episode_")
@ToString
@EqualsAndHashCode(of = "dramaEpisodeId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DramaEpisode {
    private static final int SUMMARY_MAX_LENGTH = 500;

    @Id
    @Column(name = "drama_episode_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long dramaEpisodeId;

    @Column
    private String title;

    @Column
    @Size(max = SUMMARY_MAX_LENGTH)
    private String summary;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong durationMillis;

    @Embedded
    private Like episodeLike;

    @ManyToOne
    @JoinColumn(name = "drama_id")
    private Drama drama;

    @Embedded
    @AssociationOverride(name = "dramaEpisodeComments",
                         joinTable = @JoinTable(name = JoinTableName.DRAMA_EPISODE__HAS__DRAMA_EPISODE_COMMENT,
                                                joinColumns = @JoinColumn(name = "drama_episode_id"),
                                                inverseJoinColumns = @JoinColumn(name = "drama_episode_comment_id")))
    private DramaEpisodeComments dramaEpisodeComments;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong number;

    public static class View {
        public static class Create {
            @Data
            public static class Request {
                @NotBlank
                private String title;

                @JsonUnwrapped
                private PositiveOrZeroLong order;

                @JsonUnwrapped
                private PositiveOrZeroLong durationSecond;
            }
        }
    }
}
