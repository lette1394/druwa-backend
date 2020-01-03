package me.druwa.be.domain.drama_episode_comment.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.user.model.User;

@Entity
@EqualsAndHashCode(of = "dramaEpisodeCommentId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drama_episode_comment_")
@Builder
public class DramaEpisodeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long dramaEpisodeCommentId;

    @NotNull
    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong depth;

    @Column
    @NotBlank
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private DramaEpisodeComment next;

    @ManyToOne(fetch = FetchType.LAZY)
    private DramaEpisodeComment prev;

    @Embedded
    @Builder.Default
    @NotNull
    private DramaEpisodeCommentLike commentLike = new DramaEpisodeCommentLike();

    @ManyToMany
    @Builder.Default
    @NotNull
    private Set<User> likeUsers = new HashSet<>();

    @NotNull
    @ManyToOne
    private User writtenBy;

    @NotNull
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
                                   .id(dramaEpisodeCommentId)
                                   .createdAt(timestamp.getCreatedAt())
                                   .build();
    }

    @Transactional
    public DramaEpisodeCommentLike doLike(final User user) {
        if (likeUsers.contains(user)) {
            return commentLike;
        }
        likeUsers.add(user);
        return commentLike.doLike();
    }

    @Transactional
    public DramaEpisodeCommentLike doDislike(final User user) {
        if (likeUsers.contains(user)) {
            likeUsers.remove(user);
            return commentLike.doDislike();
        }
        return commentLike;
    }

    @Data
    public static class View {
        @Data
        public static class Create {
            @Data
            public static class Request {
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
        public static class Like {
            @Data
            public static class Response {
                @NotNull
                private Long like;
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
