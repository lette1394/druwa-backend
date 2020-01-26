package me.druwa.be.domain.drama.model;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.model.Users;

import static org.assertj.core.api.Assertions.*;

class LikeOrDislikeTest {
    private LikeOrDislike likeOrDislike;

    private User user0;
    private User user1;

    @BeforeEach
    void before() {
        final Users likeUsers = Users.users(new HashSet<>());
        final PositiveOrZeroLong likeCount = new PositiveOrZeroLong(0L);

        final Users dislikeUsers = Users.users(new HashSet<>());
        final PositiveOrZeroLong dislikeCount = new PositiveOrZeroLong(0L);

        likeOrDislike = new LikeOrDislike(likeCount, likeUsers, dislikeCount, dislikeUsers);

        user0 = new User();
        user0.setUserId(0L);

        user1 = new User();
        user1.setUserId(1L);
    }

    @Test
    void doLike() {
        User user0 = new User();
        user0.setUserId(0L);

        likeOrDislike.doLike(user0);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(1L, 0L));
    }

    @Test
    void doLikeTwice() {
        likeOrDislike.doLike(user0);
        likeOrDislike.doLike(user0);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(1L, 0L));
    }

    @Test
    void doLikeTwiceOtherUser() {
        likeOrDislike.doLike(user0);
        likeOrDislike.doLike(user1);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(2L, 0L));
    }

    @Test
    void doDisLike() {
        likeOrDislike.doDislike(user0);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(0L, 1L));
    }

    @Test
    void doDisLikeTwice() {
        likeOrDislike.doDislike(user0);
        likeOrDislike.doDislike(user0);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(0L, 1L));
    }

    @Test
    void doDisLikeTwiceOtherUser() {
        likeOrDislike.doDislike(user0);
        likeOrDislike.doDislike(user1);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(0L, 2L));
    }

    @Test
    void likeToDislike() {
        likeOrDislike.doLike(user0);
        likeOrDislike.doDislike(user0);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(0L, 1L));
    }

    @Test
    void dislikeToLike() {
        likeOrDislike.doDislike(user0);
        likeOrDislike.doLike(user0);
        assertThat(likeOrDislike.toResponse()).isEqualTo(response(1L, 0L));
    }

    private LikeOrDislike.View.Read.Response response(final Long like, final Long dislike) {
        return LikeOrDislike.View.Read.Response.builder()
                                               .like(like)
                                               .dislike(dislike)
                                               .build();
    }
}