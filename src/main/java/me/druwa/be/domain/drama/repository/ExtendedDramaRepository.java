package me.druwa.be.domain.drama.repository;

import me.druwa.be.domain.drama.model.Dramas;
import me.druwa.be.domain.drama.model.DramaSearchQuery;

public interface ExtendedDramaRepository {
    Dramas search(final DramaSearchQuery dramaSearchQuery);
}
