package me.druwa.be.domain.post.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import me.druwa.be.domain.comment.model.Comment;

@Entity
@Table(name = "post_")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    public Optional<Comment> getLastComment() {
        if (comments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(comments.get(comments.size() - 1));
    }
}
