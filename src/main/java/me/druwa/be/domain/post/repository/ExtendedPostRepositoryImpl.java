package me.druwa.be.domain.post.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import me.druwa.be.domain.post.model.Post;

@Transactional
class ExtendedPostRepositoryImpl extends QuerydslRepositorySupport implements ExtendedPostRepository {
    public ExtendedPostRepositoryImpl() {
        super(Post.class);
    }
}
