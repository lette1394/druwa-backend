package me.druwa.be.domain.user.model;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "users")
public class Users {
    private final List<User> users;
}
