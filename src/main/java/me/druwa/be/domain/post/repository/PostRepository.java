package me.druwa.be.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import me.druwa.be.domain.post.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, ExtendedPostRepository {
}
