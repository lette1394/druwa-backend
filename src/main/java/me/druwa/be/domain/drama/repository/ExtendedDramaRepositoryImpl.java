package me.druwa.be.domain.drama.repository;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.model.DramaSearchQuery;
import me.druwa.be.domain.drama.model.Dramas;
import me.druwa.be.domain.drama.model.DramaPopularType;
import me.druwa.be.domain.drama.model.QDrama;
import me.druwa.be.domain.drama.model.QUserLikesDrama;
import me.druwa.be.domain.drama_tag.DramaTags;

class ExtendedDramaRepositoryImpl extends QuerydslRepositorySupport implements ExtendedDramaRepository {
    public ExtendedDramaRepositoryImpl() {
        super(Drama.class);
    }

    @Override
    public Dramas search(final DramaSearchQuery dramaSearchQuery) {
        final QDrama drama = QDrama.drama;

        final BooleanExpression matchTitle = dramaSearchQuery.titles()
                                                             .map(drama.title::likeIgnoreCase)
                                                             .reduce(BooleanExpression::or)
                                                             .orElse(Expressions.TRUE);

        return Dramas.dramas(from(drama).where(matchTitle.or(matchTags(dramaSearchQuery.tags())))
                                        .fetch());
    }

    @Override
    public Dramas findPopulars(final LocalDateTime from,
                               final LocalDateTime to,
                               final DramaPopularType dramaPopularType,
                               final DramaTags tags,
                               final Long limit) {
        final QDrama drama = QDrama.drama;
        final QUserLikesDrama userLikesDrama = QUserLikesDrama.userLikesDrama;

        return Dramas.dramas(from(drama).innerJoin(userLikesDrama)
                                        .on(drama.dramaId.eq(userLikesDrama.dramaLikeId.dramaId)
                                                         .and(dramaPopularType.query(from, to)))
                                        .where(matchTags(tags))
                                        .limit(limit)
                                        .groupBy(drama)
                                        .orderBy(dramaPopularType.order())
                                        .fetch());
    }

    @Override
    public Dramas findRandom(final Long limit) {
        final QDrama drama = QDrama.drama;

        return Dramas.dramas(from(drama).limit(limit)
                                        .orderBy(NumberExpression.random().asc())
                                        .fetch());
    }

    private BooleanExpression matchTags(final DramaTags tags) {
        final QDrama drama = QDrama.drama;

        return tags.rawNames().stream()
                   .map(drama.dramaTags.dramaTags.any().tagName::likeIgnoreCase)
                   .reduce(BooleanExpression::or)
                   .orElse(Expressions.TRUE.isTrue());
    }
}
