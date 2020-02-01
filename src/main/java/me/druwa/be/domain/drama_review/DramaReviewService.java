package me.druwa.be.domain.drama_review;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DramaReviewService {
    private final DramaReviewRepository dramaReviewRepository;

    public DramaReview save(final DramaReview dramaReview) {
        return dramaReviewRepository.save(dramaReview);
    }

    public DramaReviews findAll(final Long dramaId, final Pageable pageable) {
        return null;
    }
}
