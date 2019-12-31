package me.druwa.be.domain.comment.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import me.druwa.be.domain.comment.model.Comment;

class ExtendedCommentRepositoryImpl extends QuerydslRepositorySupport
        implements ExtendedCommentRepository {

    public ExtendedCommentRepositoryImpl() {
        super(Comment.class);
    }
}
