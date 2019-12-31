package me.druwa.be.domain.comment.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import me.druwa.be.domain.comment.repository.CommentRepository;
import me.druwa.be.domain.comment.model.Comment;
import me.druwa.be.domain.post.model.Post;
import me.druwa.be.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;

    private final CommentRepository commentRepository;

    public Comment create(final Comment.View.Create.Request request) {
        return null;
    }

    public Comment append(Comment from) {
        final Post post = postService.find(1);
        final Comment comment = post.getLastComment()
                                    .orElseThrow(() -> new NoSuchElementException(String.format("")));

        return null;
    }
}
