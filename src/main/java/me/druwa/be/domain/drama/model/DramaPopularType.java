package me.druwa.be.domain.drama.model;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.drama_review.QDramaReview;

@RequiredArgsConstructor
public enum DramaPopularType {
    LIKE(QUserLikesDrama.userLikesDrama.timestamp.updatedAt::between,
               QDrama.drama.dramaLike.likeCount.count().desc());
//    DRAMA_REVIEW(QDramaReview.dramaReview.timestamp.createdAt::between,
//                 QDrama.drama.dramaReviews.dramaReviews.size().desc());

    private final BiFunction<LocalDateTime, LocalDateTime, BooleanExpression> query;
    private final OrderSpecifier<?> order;

    public BooleanExpression query(LocalDateTime from, LocalDateTime to) {
        return query.apply(from, to);
    }

    public OrderSpecifier<?> order() {
        return order;
    }
}
