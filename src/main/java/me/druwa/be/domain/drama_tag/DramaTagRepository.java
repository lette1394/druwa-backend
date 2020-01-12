package me.druwa.be.domain.drama_tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DramaTagRepository extends JpaRepository<DramaTag, String>, DramaTagRepositoryExtended {

    default DramaTags saveAll(final DramaTags dramaTags) {
        return new DramaTags(saveAll(dramaTags.raw()));
    }
}
