package me.druwa.be.domain.drama.repository;

import java.time.LocalDateTime;

import me.druwa.be.domain.drama.model.Dramas;
import me.druwa.be.domain.drama.model.DramaSearchQuery;
import me.druwa.be.domain.drama.model.DramaPopularType;
import me.druwa.be.domain.drama_tag.DramaTags;

public interface ExtendedDramaRepository {
    Dramas search(final DramaSearchQuery dramaSearchQuery);

    Dramas findPopulars(final LocalDateTime from,
                        final LocalDateTime to,
                        final DramaPopularType dramaPopularType,
                        final DramaTags tags, final Long limit);

    Dramas findRandom(final Long dramaId, final Long limit);
}
