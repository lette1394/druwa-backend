package me.druwa.be.domain.episode.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.model.Users;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeCommentLike {
    @Column
    @NotNull
    @Convert(converter = PositiveLongConverter.class)
    private PositiveOrZeroLong likeCount;

    @Column
    @NotNull
    @Convert(converter = PositiveLongConverter.class)
    private PositiveOrZeroLong dislikeCount;

    @NotNull
    @Embedded
    private Users byUsers;

    @PrePersist
    public void onCreate() {
        likeCount = dislikeCount = new PositiveOrZeroLong(0L);
    }

    public EpisodeCommentLike doLike(final User user) {
        if (byUsers.has(user)) {
            return this;
        }
        likeCount = new PositiveOrZeroLong(likeCount.getValue() + 1);
        byUsers.append(user);
        return this;
    }

    public EpisodeCommentLike doDislike(final User user) {
        if (byUsers.has(user)) {
            dislikeCount = new PositiveOrZeroLong(dislikeCount.getValue() + 1);
            byUsers.remove(user);
            return this;
        }
        return this;
    }

    public long sum() {
        return likeCount.getValue() - dislikeCount.getValue();
    }

    public View.Create.Response toResponse() {
        return View.Create.Response.builder()
                                   .like(sum())
                                   .build();
    }

    @Data
    public static class View {
        @Data
        public static class Create {
            @Data
            public static class Response {
                @Builder
                public Response(final long like) {
                    this.like = like;
                }

                private long like;
            }
        }
    }
}
