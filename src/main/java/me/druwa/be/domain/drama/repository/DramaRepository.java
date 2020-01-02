package me.druwa.be.domain.drama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.druwa.be.domain.drama.model.Drama;

@Repository
public interface DramaRepository extends JpaRepository<Drama, Long>,
                                         ExtendedDramaRepository {

}
