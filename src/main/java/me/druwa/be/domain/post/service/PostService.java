package me.druwa.be.domain.post.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import me.druwa.be.domain.post.model.Post;
import me.druwa.be.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public Post find(final long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(NoSuchElementException::new);
    }
}
