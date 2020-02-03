package me.druwa.be.domain.drama_review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DramaReviewRepository extends JpaRepository<DramaReview, Long>, DramaReviewRepositoryExtended {

}
