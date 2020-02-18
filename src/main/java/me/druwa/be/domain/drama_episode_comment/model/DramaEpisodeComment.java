package me.druwa.be.domain.drama_episode_comment.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;

import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.db.JoinTableName;
import me.druwa.be.domain.common.model.IgnoreMerge;
import me.druwa.be.domain.common.model.Mergeable;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.drama.model.LikeOrDislike;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.global.exception.DruwaException;

@Entity
@EqualsAndHashCode(of = "dramaEpisodeCommentId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drama_episode_comment_")
@Builder
public class DramaEpisodeComment implements Mergeable<DramaEpisodeComment> {
    private static final int MIN_COMMENT_CONTENTS_LENGTH = 1;
    private static final int MAX_COMMENT_CONTENTS_LENGTH = 500;

    @Id
    @Column(name = "drama_episode_comment_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @IgnoreMerge
    private Long dramaEpisodeCommentId;

    @NotNull
    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong depth;

    @Column
    @NotBlank
    @Size(min = MIN_COMMENT_CONTENTS_LENGTH, max = MAX_COMMENT_CONTENTS_LENGTH)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private DramaEpisodeComment next;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private DramaEpisodeComment prev;

    @Embedded
    @AssociationOverrides({
                                  @AssociationOverride(name = "likeUsers.users",
                                                       joinTable = @JoinTable(name = JoinTableName.USER__LIKES__DRAMA_EPISODE_COMMENT,
                                                                              joinColumns = @JoinColumn(name = "drama_episode_comment_id"),
                                                                              inverseJoinColumns = @JoinColumn(name = "user_id"))),
                                  @AssociationOverride(name = "dislikeUsers.users",
                                                       joinTable = @JoinTable(name = JoinTableName.USER__DISLIKES__DRAMA_EPISODE_COMMENT,
                                                                              joinColumns = @JoinColumn(name = "drama_episode_comment_id"),
                                                                              inverseJoinColumns = @JoinColumn(name = "user_id")))
                          })
    private LikeOrDislike dramaEpisodeCommentLike;

    @ManyToOne
    private DramaEpisode dramaEpisode;

    @NotNull
    @ManyToOne
    private User writtenBy;

    @NotNull
    @Embedded
    private Timestamp timestamp;

    @PrePersist
    public void onCreate() {
        timestamp = Timestamp.now();
        dramaEpisodeCommentLike = new LikeOrDislike();
    }

    @PreUpdate
    public void onUpdate() {
        timestamp.onUpdate();
    }

    public View.Create.Response toCreateResponse() {
        return View.Create.Response.builder()
                                   .id(dramaEpisodeCommentId)
                                   .createdAt(timestamp.getCreatedAt())
                                   .prev(getId(prev))
                                   .next(getId(next))
                                   .build();
    }

    public View.Read.Response toReadResponse(final User user) {
        return View.Read.Response.builder()
                                 .id(dramaEpisodeCommentId)
                                 .contents(contents)
                                 .depth(depth)
                                 .prev(getId(prev))
                                 .timestamp(timestamp)
                                 .like(dramaEpisodeCommentLike.toResponse(user))
                                 .isRoot(getId(prev) == -1L)
                                 .writtenByMe(writtenBy.equals(user))
                                 .user(writtenBy.toReadResponse())
                                 .build();

    }

    @Transactional
    public LikeOrDislike doLike(final User user) {
        return dramaEpisodeCommentLike.doLike(user);
    }

    @Transactional
    public LikeOrDislike doDislike(final User user) {
        return dramaEpisodeCommentLike.doDislike(user);
    }

    private Long getId(DramaEpisodeComment comment) {
        if (Objects.nonNull(comment) && Objects.nonNull(comment.dramaEpisodeCommentId)) {
            return comment.dramaEpisodeCommentId;
        }
        return -1L;
    }

    @Data
    public static class View {

        @Data
        public static class Create {
            @Data
            @Builder
            public static class Request {
                @Builder.Default
                private PositiveOrZeroLong depth = PositiveOrZeroLong.positiveOrZeroLong(0L);
                @NotBlank
                @Size(min = MIN_COMMENT_CONTENTS_LENGTH, max = MAX_COMMENT_CONTENTS_LENGTH)
                private String contents;

                @Builder(builderMethodName = "toPartialDramaEpisodeComment")
                public DramaEpisodeComment toPartialDramaEpisodeCommentBuilder(final DramaEpisodeComment prev,
                                                                               final User writtenBy,
                                                                               final DramaEpisode dramaEpisode,
                                                                               final Long depth) {
                    Long newDepth = null;
                    if (Objects.nonNull(depth)) {
                        newDepth = depth;
                    } else {
                        newDepth = this.depth != null ? this.depth.value() : 0;
                    }
                    return DramaEpisodeComment.builder()
                                              .dramaEpisode(dramaEpisode)
                                              .prev(prev)
                                              .writtenBy(writtenBy)
                                              .contents(contents)
                                              .depth(PositiveOrZeroLong.positiveOrZeroLong(newDepth))
                                              .build();
                }
            }

            @Data
            @Builder
            public static class Response {
                private Long id;
                private Long prev;
                private Long next;
                private LocalDateTime createdAt;
            }
        }

        @Data
        public static class Update {
            @Data
            public static class Request {
                @NotBlank
                @Size(min = MIN_COMMENT_CONTENTS_LENGTH, max = MAX_COMMENT_CONTENTS_LENGTH)
                private String contents;

                public DramaEpisodeComment toPartialDramaEpisodeComment() {
                    return DramaEpisodeComment.builder()
                                              .contents(contents)
                                              .build();
                }
            }
        }

        @Data
        public static class Like {
            @Data
            @Builder
            public static class Response {
                private Long like;
            }
        }

        @Data
        public static class Read {
            @Data
            @Builder
            public static class Response implements Comparable<Response> {
                private Long id;
                @JsonUnwrapped
                private PositiveOrZeroLong depth;
                @NotBlank
                @Size(min = MIN_COMMENT_CONTENTS_LENGTH, max = MAX_COMMENT_CONTENTS_LENGTH)
                private String contents;
                @JsonUnwrapped
                private LikeOrDislike.View.Read.Response like;
                @JsonUnwrapped
                private Timestamp timestamp;

                private Long prev;

                @Builder.Default
                private Boolean isRoot = false;

                private Boolean writtenByMe;

                private User.View.Read.Response user;

                @Override
                public int compareTo(final Response o) {
                    return Long.compare(this.id, o.id);
                }
            }
        }
    }
}
