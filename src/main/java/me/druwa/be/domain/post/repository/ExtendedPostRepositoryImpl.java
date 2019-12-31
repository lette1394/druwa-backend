package me.druwa.be.domain.post.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import me.druwa.be.domain.post.model.Post;

@Transactional
class ExtendedPostRepositoryImpl extends QuerydslRepositorySupport implements ExtendedPostRepository {
    public ExtendedPostRepositoryImpl() {
        super(Post.class);
    }
}
