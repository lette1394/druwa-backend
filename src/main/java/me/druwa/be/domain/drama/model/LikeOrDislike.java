package me.druwa.be.domain.drama.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.model.Users;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LikeOrDislike {
    @Column
    @NotNull
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong likeCount = new PositiveOrZeroLong(0L);

    @Embedded
    private Users likeUsers;

    @Column
    @NotNull
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong dislikeCount = new PositiveOrZeroLong(0L);

    @Embedded
    private Users dislikeUsers;

    @PrePersist
    public void onCreate() {
        likeCount = new PositiveOrZeroLong(0L);
        dislikeCount = new PositiveOrZeroLong(0L);
    }

    public LikeOrDislike doLike(final User user) {
        if (likeUsers.contains(user)) {
            likeUsers.remove(user);
            likeCount.decrease();
            return this;
        }

        if (dislikeUsers.contains(user)) {
            dislikeCount.decrease();
            dislikeUsers.remove(user);
        }

        likeCount.increase();
        likeUsers.add(user);
        return this;
    }

    public LikeOrDislike doDislike(final User user) {
        if (dislikeUsers.contains(user)) {
            dislikeUsers.remove(user);
            dislikeCount.decrease();
            return this;
        }

        if (likeUsers.contains(user)) {
            likeCount.decrease();
            likeUsers.remove(user);
        }

        dislikeCount.increase();
        dislikeUsers.add(user);
        return this;
    }

    public Long sum() {
        return likeCount.getValue() - dislikeCount.getValue();
    }

    @JsonProperty("like")
    public Long likeSum() {
        return likeCount.getValue();
    }

    @JsonProperty("dislike")
    public Long dislikeSum() {
        return dislikeCount.getValue();
    }

    public View.Read.Response toResponse(final User user) {
        return View.Read.Response.builder()
                                 .like(likeCount.getValue())
                                 .liked(likeUsers.contains(user))
                                 .dislike(dislikeCount.getValue())
                                 .disliked(dislikeUsers.contains(user))
                                 .build();
    }

    @Data
    public static class View {
        @Data
        public static class Read {
            @Data
            @Builder
            public static class Response {
                @PositiveOrZero
                private Long like;
                @NotNull
                @Builder.Default
                private Boolean liked = false;
                @PositiveOrZero
                private Long dislike;
                @NotNull
                @Builder.Default
                private Boolean disliked = false;
            }
        }
    }
}
