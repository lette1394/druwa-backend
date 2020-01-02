package me.druwa.be.domain.episode.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.user.model.User;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "episode_comment_")
@Builder
public class EpisodeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Convert(converter = PositiveLongConverter.class)
    private PositiveOrZeroLong depth;

    @Column
    @NotBlank
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private EpisodeComment next;

    @ManyToOne(fetch = FetchType.LAZY)
    private EpisodeComment prev;

    @Embedded
    private EpisodeCommentLike commentLike;

    @ManyToOne
    private User writtenBy;

    @Embedded
    private Timestamp timestamp;

    @PrePersist
    public void onCreate() {
        timestamp = Timestamp.now();
    }

    @PreUpdate
    public void onUpdate() {
        timestamp.onUpdate();
    }

    public View.Create.Response toCreateResponse() {
        return View.Create.Response.builder()
                                   .id(id)
                                   .createdAt(timestamp.getCreatedAt())
                                   .build();
    }

    public EpisodeCommentLike doLike(final User user) {
        return commentLike.doLike(user);
    }

    public EpisodeCommentLike doDislike(final User user) {
        return commentLike.doDislike(user);
    }

    @Data
    public static class View {
        @Data
        public static class Create {
            @Data
            public static class Request {
                @NotNull
                private Long episodeId;

                @NotNull
                private PositiveOrZeroLong depth;

                @NotBlank
                private String contents;
            }

            @Data
            public static class Response {
                @Builder
                public Response(@PositiveOrZero final Long id, @NotNull final LocalDateTime createdAt) {
                    this.id = id;
                    this.createdAt = createdAt;
                }

                @PositiveOrZero
                private Long id;

                @NotNull
                private LocalDateTime createdAt;
            }
        }

        @Data
        public static class Read {
            @Data
            public static class Response {
                @Builder
                public Response(@PositiveOrZero final Long id, @NotNull final LocalDateTime createdAt) {
                    this.id = id;
                    this.createdAt = createdAt;
                }

                @PositiveOrZero
                private Long id;

                @NotNull
                private LocalDateTime createdAt;
            }
        }


    }
}
