package me.druwa.be.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.druwa.be.domain.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
                                        ExtendedUserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    boolean existsByEmail(String email);
}
