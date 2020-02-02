package me.druwa.be.domain.drama.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.model.UserLikesDrama;
import me.druwa.be.domain.user.model.User;

@Repository
public interface UserLikesDramaRepository extends JpaRepository<UserLikesDrama, UserLikesDrama.DramaLikeKey> {
    Optional<UserLikesDrama> findByDramaAndUser(final Drama drama, final User user);
}
