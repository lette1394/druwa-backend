package me.druwa.be.domain.drama_episode_comment.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComments;
import me.druwa.be.domain.drama_episode_comment.model.Like;
import me.druwa.be.domain.drama_episode_comment.repository.DramaEpisodeCommentRepository;
import me.druwa.be.domain.user.model.User;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DramaEpisodeCommentService {
    private final DramaEpisodeCommentRepository dramaEpisodeCommentRepository;

    public DramaEpisodeComment create(final DramaEpisodeComment.View.Create.Request request, final User user) {
        final DramaEpisodeComment dramaEpisodeComment = DramaEpisodeComment.builder()
                                                                           .content(request.getContents())
                                                                           .depth(request.getDepth())
                                                                           .writtenBy(user)
                                                                           .build();

        return dramaEpisodeCommentRepository.save(dramaEpisodeComment);
    }

    public void ensureExistsBy(final long commentId) {
        if (dramaEpisodeCommentRepository.existsByDramaEpisodeCommentId(commentId)) {
            return;
        }
        throw new NoSuchElementException(format("no episodeComment with id:[%s]", commentId));
    }

    public DramaEpisodeComment findBy(final long commentId) {
        return dramaEpisodeCommentRepository.findById(commentId)
                                            .orElseThrow(() -> new NoSuchElementException(
                                                    format("no episodeComment with id:[%s]",
                                                           commentId)));

    }

    @Transactional(readOnly = true)
    public DramaEpisodeComments list(final Long episodeId) {
        return dramaEpisodeCommentRepository.findByDramaEpisodeId(episodeId);
    }

    @Transactional
    public Like doLike(final User user, final long commentId) {
        return findBy(commentId).doLike(user);
    }

    @Transactional
    public Like doDislike(final User user, final long commentId) {
        return findBy(commentId).doDislike(user);
    }
}
