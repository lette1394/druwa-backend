package me.druwa.be.domain.user.model;

import java.util.List;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @OneToMany
    private Set<User> users;

    public static Users users(final List<User> users) {
        return new Users(Sets.newHashSet(users));
    }

    public static Users users(final Set<User> users) {
        return new Users(users);
    }

    public boolean has(final User user) {
        return users.contains(user);
    }

    public void append(final User user) {
        users.add(user);
    }

    public void remove(final User user) {
        users.remove(user);
    }

    public boolean contains(final User user) {
        return users.contains(user);
    }

    public boolean add(final User user) {
        return users.add(user);
    }
}
