package me.druwa.be.domain.drama.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import me.druwa.be.domain.drama.model.Drama;

class ExtendedDramaRepositoryImpl extends QuerydslRepositorySupport implements ExtendedDramaRepository {
    public ExtendedDramaRepositoryImpl() {
        super(Drama.class);
    }
}
