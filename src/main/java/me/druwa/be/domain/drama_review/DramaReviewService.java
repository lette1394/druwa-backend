package me.druwa.be.domain.drama_review;

import java.util.NoSuchElementException;

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
        return dramaReviewRepository.findAll(dramaId);
    }

    public DramaReview find(final Long reviewId) {
        return dramaReviewRepository.findById(reviewId)
                                    .orElseThrow(() -> new NoSuchElementException(String.format("no review id %s",
                                                                                                reviewId)));
    }
}
