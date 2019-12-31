package me.druwa.be.domain.user.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.model.Users;
import me.druwa.be.domain.user.model.QUser;

@Transactional(readOnly = true)
class ExtendedUserRepositoryImpl extends QuerydslRepositorySupport
        implements ExtendedUserRepository {

    public ExtendedUserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Users findRecentlyRegistered(int limit) {
        final QUser user = QUser.user;
        return Users.users(from(user).limit(limit)
                                     .orderBy(user.timestamp.createdAt.desc())
                                     .fetch());
    }
}