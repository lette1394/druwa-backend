package me.druwa.be.domain.drama.model;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
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

import static me.druwa.be.domain.common.db.JoinTableName.USER__DISLIKES__DRAMA;

@Embeddable
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class DramaLikeOrDislike {
    @Column
    @NotNull
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong likeCount = new PositiveOrZeroLong(0L);

    @Embedded
    private UserLikesDramas likeUsers;

    @Column
    @NotNull
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong dislikeCount = new PositiveOrZeroLong(0L);

    @Embedded
    @AssociationOverride(name = "users",
                         joinTable = @JoinTable(name = USER__DISLIKES__DRAMA,
                                                joinColumns = @JoinColumn(name = "drama_id"),
                                                inverseJoinColumns = @JoinColumn(name = "user_id")))
    private Users dislikeUsers;

    @PrePersist
    public void onCreate() {
        likeCount = new PositiveOrZeroLong(0L);
        dislikeCount = new PositiveOrZeroLong(0L);
    }

    public DramaLikeOrDislike doLike(final Drama drama, final User user) {
        if (likeUsers.contains(drama, user)) {
            likeUsers.remove(drama, user);
            likeCount.decrease();
            return this;
        }

        if (dislikeUsers.contains(user)) {
            dislikeCount.decrease();
            dislikeUsers.remove(user);
        }

        likeCount.increase();
        likeUsers.add(drama, user);
        return this;
    }

    public DramaLikeOrDislike doDislike(final Drama drama, final User user) {
        if (dislikeUsers.contains(user)) {
            dislikeUsers.remove(user);
            dislikeCount.decrease();
            return this;
        }

        if (likeUsers.contains(drama, user)) {
            likeCount.decrease();
            likeUsers.remove(null, null);
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

    public View.Read.Response toResponse(final Drama drama, final User user) {
        return View.Read.Response.builder()
                                 .like(likeCount.getValue())
                                 .liked(likeUsers.contains(drama, user))
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
