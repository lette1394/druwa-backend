package me.druwa.be.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.druwa.be.domain.comment.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>,
                                           ExtendedCommentRepository {
}
