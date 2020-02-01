package me.druwa.be.domain.drama.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.model.Dramas;
import me.druwa.be.domain.drama.model.QDrama;
import me.druwa.be.domain.drama.model.DramaSearchQuery;

class ExtendedDramaRepositoryImpl extends QuerydslRepositorySupport implements ExtendedDramaRepository {
    public ExtendedDramaRepositoryImpl() {
        super(Drama.class);
    }

    @Override
    public Dramas search(final DramaSearchQuery dramaSearchQuery) {
        QDrama drama = QDrama.drama;

        final BooleanExpression matchTitle = dramaSearchQuery.titles()
                                                             .map(drama.title::likeIgnoreCase)
                                                             .reduce(BooleanExpression::or)
                                                             .orElse(Expressions.TRUE);

        final BooleanExpression matchTags = dramaSearchQuery.tags()
                                                            .map(drama.dramaTags.dramaTags.any().tagName::likeIgnoreCase)
                                                            .reduce(BooleanExpression::or)
                                                            .orElse(Expressions.TRUE);

        return Dramas.dramas(from(drama).where(matchTitle.or(matchTags))
                                        .fetch());
    }
}
