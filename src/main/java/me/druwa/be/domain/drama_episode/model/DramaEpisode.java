package me.druwa.be.domain.drama_episode.model;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.db.Image;
import me.druwa.be.domain.common.db.JoinTableName;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.model.LikeOrDislike;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComments;
import me.druwa.be.domain.user.model.User;

import static me.druwa.be.domain.common.model.PositiveOrZeroLong.positiveOrZeroLong;

@Entity
@Table(name = "drama_episode_")
@ToString(of = { "dramaEpisodeId", "title" })
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
    private String playUrl;

    @Column
    @Size(max = SUMMARY_MAX_LENGTH)
    private String summary;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong durationInMillis;

    @Embedded
    private DramaEpisodeImages dramaEpisodeImages;

    @Embedded
    @AssociationOverrides({
                                  @AssociationOverride(name = "likeUsers.users",
                                                       joinTable = @JoinTable(name = JoinTableName.USER__LIKES__DRAMA_EPISODE,
                                                                              joinColumns = @JoinColumn(name = "drama_episode_id"),
                                                                              inverseJoinColumns = @JoinColumn(name = "user_id"))),
                                  @AssociationOverride(name = "dislikeUsers.users",
                                                       joinTable = @JoinTable(name = JoinTableName.USER__DISLIKES__DRAMA_EPISODE,
                                                                              joinColumns = @JoinColumn(name = "drama_episode_id"),
                                                                              inverseJoinColumns = @JoinColumn(name = "user_id")))
                          })
    private LikeOrDislike dramaEpisodeLike;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong episodeNumber;

    @NotNull
    @ManyToOne
    private User registeredBy;

    @Embedded
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "drama_id")
    private Drama drama;

    @Embedded
//    @AssociationOverride(name = "dramaEpisodeComments",
//                         joinTable = @JoinTable(name = JoinTableName.DRAMA_EPISODE__HAS__DRAMA_EPISODE_COMMENT,
//                                                joinColumns = @JoinColumn(name = "drama_episode_id"),
//                                                inverseJoinColumns = @JoinColumn(name = "drama_episode_comment_id")))
    private DramaEpisodeComments dramaEpisodeComments;


    public DramaEpisode merge(final DramaEpisodeImages dramaEpisodeImages) {
        this.dramaEpisodeImages.merge(dramaEpisodeImages);
        return this;
    }

    @PrePersist
    public void onCreate() {
        timestamp = Timestamp.now();
        dramaEpisodeLike = new LikeOrDislike();
    }

    @PreUpdate
    public void onUpdate() {
        timestamp.onUpdate();
    }

    public View.Read.Response toReadResponse() {
        return View.Read.Response.builder()
                                 .dramaEpisodeId(dramaEpisodeId)
                                 .title(title)
                                 .dramaTitle(drama.getTitle())
                                 .summary(summary)
                                 .durationInMillis(durationInMillis)
                                 .likeOrDislike(dramaEpisodeLike)
                                 .episodeNumber(episodeNumber)
                                 .playUrl(playUrl)
                                 .totalComments(dramaEpisodeComments.count())
                                 .timestamp(timestamp)
                                 .build();
    }

    public View.Create.Response toCreateResponse() {
        return View.Create.Response.builder()
                                   .dramaEpisodeId(dramaEpisodeId)
                                   .build();
    }

    public Set<Image.View.Read.Response> toImageOnlyResponse() {
        return this.dramaEpisodeImages.toRaw().stream()
                                      .map(Image::toResponse)
                                      .collect(Collectors.toSet());
    }

    public static class View {
        public static class Create {
            @Data
            public static class Request {
                @NotBlank
                private String title;
                @NotBlank
                private String summary;
                @NotBlank
                private String playUrl;
                @Positive
                @NotNull
                private Long episodeNumber;
                @Positive
                @NotNull
                private Long durationInMillis;

                @Builder(builderMethodName = "toPartialDramaEpisode")
                public DramaEpisode toPartialDramaEpisodeBuilder(final Drama drama, final User user) {
                    return DramaEpisode.builder()
                                       .drama(drama)
                                       .registeredBy(user)
                                       .title(title)
                                       .playUrl(playUrl)
                                       .summary(summary)
                                       .episodeNumber(positiveOrZeroLong(episodeNumber))
                                       .durationInMillis(positiveOrZeroLong(durationInMillis))
                                       .build();
                }
            }

            @Data
            @Builder
            public static class Response {
                @Positive
                private Long dramaEpisodeId;
            }
        }

        public static class Read {
            @Data
            @Builder
            public static class Response implements Comparable<Response> {
                @Positive
                private Long dramaEpisodeId;
                @NotBlank
                private String title;
                @NotBlank
                private String dramaTitle;
                @NotBlank
                private String summary;
                @NotBlank
                private String playUrl;
                @JsonUnwrapped
                private PositiveOrZeroLong durationInMillis;
                @JsonUnwrapped
                private LikeOrDislike likeOrDislike;
                @JsonUnwrapped
                private PositiveOrZeroLong episodeNumber;
                @PositiveOrZero
                private Integer totalComments;
                @JsonUnwrapped
                private Timestamp timestamp;

                @Override
                public int compareTo(final Response o) {
                    return Long.compare(this.episodeNumber.getValue(), o.episodeNumber.getValue());
                }
            }
        }

        public static class Search {
            @Data
            public static class Response {
                @Positive
                private Long dramaEpisodeId;
                @NotBlank
                private String title;
                @NotBlank
                private String dramaTitle;
                @JsonUnwrapped
                public PositiveOrZeroLong episodeNumber;
                @NotBlank
                private String productionCompany;
            }
        }
    }
}
