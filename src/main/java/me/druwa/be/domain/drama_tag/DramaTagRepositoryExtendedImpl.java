package me.druwa.be.domain.drama_tag;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

class DramaTagRepositoryExtendedImpl extends QuerydslRepositorySupport implements DramaTagRepositoryExtended {
    DramaTagRepositoryExtendedImpl() {
        super(DramaTag.class);
    }

    @Override
    public DramaTags tryFindAll(final DramaTags dramaTags) {
        final QDramaTag dramaTag = QDramaTag.dramaTag;

        return new DramaTags(from(dramaTag).where(dramaTag.tagName.in(dramaTags.rawNames()))
                                           .fetch());
    }
}
