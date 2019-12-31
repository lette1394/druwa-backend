package me.druwa.be.domain.user.repository;

import me.druwa.be.domain.user.model.Users;

public interface ExtendedUserRepository {
    Users findRecentlyRegistered(int limit);
}


