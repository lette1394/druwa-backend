package me.druwa.be.domain.drama_episode_comment.model;

import java.time.LocalDateTime;
import java.util.Objects;
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

import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
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
    private Like commentLike = new Like();

    @ManyToOne
    private DramaEpisode dramaEpisode;

    @ManyToMany
    private Set<User> likeUsers;

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

    public View.Read.Response toReadResponse() {
        return View.Read.Response.builder()
                                 .id(dramaEpisodeCommentId)
                                 .contents(content)
                                 .depth(depth)
                                 .timestamp(timestamp)
                                 .like(commentLike.sum())
                                 .next(Objects.isNull(next) ? -1 : next.dramaEpisodeCommentId)
                                 .prev(Objects.isNull(prev) ? -1 : prev.dramaEpisodeCommentId)
                                 .build();

    }

    @Transactional
    public Like doLike(final User user) {
        if (likeUsers.contains(user)) {
            return commentLike;
        }
        likeUsers.add(user);
        return commentLike.doLike();
    }

    @Transactional
    public Like doDislike(final User user) {
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
            @Builder
            public static class Request {
                private PositiveOrZeroLong depth;
                private String contents;
            }

            @Data
            @Builder
            public static class Response {
                private Long id;
                private LocalDateTime createdAt;
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
            public static class Response {
                private Long id;

                @JsonUnwrapped
                private PositiveOrZeroLong depth;

                @JsonInclude
                private Long next;

                @JsonInclude
                private Long prev;

                private String contents;

                private Long like;

                @JsonUnwrapped
                private Timestamp timestamp;
            }
        }
    }
}
