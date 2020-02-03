package me.druwa.be.domain.drama_review;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

class DramaReviewRepositoryExtendedImpl extends QuerydslRepositorySupport
        implements DramaReviewRepositoryExtended {

    public DramaReviewRepositoryExtendedImpl() {
        super(DramaReview.class);
    }

    @Override
    public DramaReviews findAll(final Long dramaId) {
        final QDramaReview dramaReview = QDramaReview.dramaReview;

        return DramaReviews.dramaReviews(from(dramaReview).where(dramaReview.drama.dramaId.eq(dramaId))
                                                 .fetch());
    }
}
