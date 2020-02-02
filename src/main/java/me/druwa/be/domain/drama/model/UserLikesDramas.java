package me.druwa.be.domain.drama.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import me.druwa.be.domain.user.model.User;

@Embeddable
public class UserLikesDramas {
    @OneToMany(mappedBy = "drama", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<UserLikesDrama> userLikesDramas;

    public boolean contains(final Drama drama, final User user) {
        return userLikesDramas.contains(newLike(drama, user));
    }

    public void remove(final Drama drama, final User user) {
        userLikesDramas.remove(newLike(drama, user));
    }

    public void add(final Drama drama, final User user) {
        userLikesDramas.add(newLike(drama, user));
    }

    private UserLikesDrama newLike(final Drama drama, final User user) {
        return UserLikesDrama.builder()
                             .drama(drama)
                             .user(user)
                             .build();
    }
}
