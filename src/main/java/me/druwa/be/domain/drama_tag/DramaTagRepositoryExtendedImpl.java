package me.druwa.be.domain.drama_tag;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

class DramaTagRepositoryExtendedImpl extends QuerydslRepositorySupport implements DramaTagRepositoryExtended {
    DramaTagRepositoryExtendedImpl() {
        super(DramaTag.class);
    }

    @Override
    public DramaTags findAll(final DramaTags dramaTags) {
        final QDramaTag dramaTag = QDramaTag.dramaTag;

        return new DramaTags(
                from(dramaTag).where(dramaTag.tagName.in(dramaTags.rawNames()))
                              .fetch());
    }

    @Override
    public DramaTags findAll(final DramaTagSearchStrings searchWords) {
        final QDramaTag dramaTag = QDramaTag.dramaTag;
        final BooleanExpression allWords = searchWords.stream()
                                                      .map(dramaTag.tagName::containsIgnoreCase)
                                                      .reduce(BooleanExpression::or)
                                                      .orElse(Expressions.TRUE);

        return new DramaTags(from(dramaTag).where(allWords)
                                           .fetch());
    }
}
