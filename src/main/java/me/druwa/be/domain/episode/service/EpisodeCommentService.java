package me.druwa.be.domain.episode.service;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.episode.model.EpisodeComment;
import me.druwa.be.domain.episode.model.EpisodeCommentLike;
import me.druwa.be.domain.episode.respository.EpisodeCommentRepository;
import me.druwa.be.domain.post.model.Post;
import me.druwa.be.domain.post.service.PostService;
import me.druwa.be.domain.user.model.User;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class EpisodeCommentService {
    private final PostService postService;

    private final EpisodeCommentRepository episodeCommentRepository;

    public EpisodeComment create(final EpisodeComment.View.Create.Request request, final User user) {
        final EpisodeComment episodeComment = EpisodeComment.builder()
                                                            .content(request.getContents())
                                                            .depth(request.getDepth())
                                                            .writtenBy(user)
                                                            .build();


        return episodeCommentRepository.save(episodeComment);
    }

    public EpisodeComment append(EpisodeComment from) {
        final Post post = postService.find(1);
        final EpisodeComment episodeComment = post.getLastComment()
                                                  .orElseThrow(() -> new NoSuchElementException(format("")));

        return null;
    }

    public void ensureExistsBy(final long commentId) {
        if (episodeCommentRepository.existsById(commentId)) {
            return;
        }
        throw new NoSuchElementException(format("no episodeComment with id:[%s]", commentId));
    }

    public EpisodeComment findBy(final long commentId) {
        return episodeCommentRepository.findById(commentId)
                                       .orElseThrow(() -> new NoSuchElementException(
                                               format("no episodeComment with id:[%s]",
                                                      commentId)));

    }

    @Transactional
    public EpisodeCommentLike doLike(final User user, final long commentId) {
        return findBy(commentId).doLike(user);
    }

    @Transactional
    public EpisodeCommentLike doDislike(final User user, final long commentId) {
        return findBy(commentId).doDislike(user);
    }
}
