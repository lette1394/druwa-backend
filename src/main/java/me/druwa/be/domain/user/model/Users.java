package me.druwa.be.domain.user.model;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Users {
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
}
