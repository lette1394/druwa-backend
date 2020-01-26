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
        assertThat(likeOrDislike.likeSum()).isEqualTo(1);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(0);
    }

    @Test
    void doLikeTwice() {
        likeOrDislike.doLike(user0);
        likeOrDislike.doLike(user0);
        assertThat(likeOrDislike.likeSum()).isEqualTo(0);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(0);
    }

    @Test
    void doLikeTwiceOtherUser() {
        likeOrDislike.doLike(user0);
        likeOrDislike.doLike(user1);
        assertThat(likeOrDislike.likeSum()).isEqualTo(2L);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(0);
    }

    @Test
    void doDisLike() {
        likeOrDislike.doDislike(user0);
        assertThat(likeOrDislike.likeSum()).isEqualTo(0);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(1);
    }

    @Test
    void doDisLikeTwice() {
        likeOrDislike.doDislike(user0);
        likeOrDislike.doDislike(user0);
        assertThat(likeOrDislike.likeSum()).isEqualTo(0);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(0);
    }

    @Test
    void doDisLikeTwiceOtherUser() {
        likeOrDislike.doDislike(user0);
        likeOrDislike.doDislike(user1);
        assertThat(likeOrDislike.likeSum()).isEqualTo(0);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(2);
    }

    @Test
    void likeToDislike() {
        likeOrDislike.doLike(user0);
        likeOrDislike.doDislike(user0);
        assertThat(likeOrDislike.likeSum()).isEqualTo(0);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(1);
    }

    @Test
    void dislikeToLike() {
        likeOrDislike.doDislike(user0);
        likeOrDislike.doLike(user0);
        assertThat(likeOrDislike.likeSum()).isEqualTo(1);
        assertThat(likeOrDislike.dislikeSum()).isEqualTo(0);
    }
}