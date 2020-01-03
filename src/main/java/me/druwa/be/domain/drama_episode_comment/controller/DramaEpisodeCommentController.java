package me.druwa.be.domain.drama_episode_comment.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.drama_episode.service.DramaEpisodeService;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeCommentLike;
import me.druwa.be.domain.drama_episode_comment.service.DramaEpisodeCommentService;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaEpisodeCommentController {
    private final DramaService dramaService;
    private final DramaEpisodeService dramaEpisodeService;
    private final DramaEpisodeCommentService dramaEpisodeCommentService;

    @PostMapping("/dramas/{dramaId}/episodes/{episodeId}/comments")
    public ResponseEntity<?> create(@Valid
                                    @RequestBody DramaEpisodeComment.View.Create.Request body,
                                    @CurrentUser User user,
                                    @PathVariable final long dramaId,
                                    @PathVariable final long episodeId) {
        dramaService.ensureExistsBy(dramaId);
        dramaEpisodeService.ensureExistsBy(episodeId);

        final DramaEpisodeComment dramaEpisodeComment = dramaEpisodeCommentService.create(body, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(dramaEpisodeComment.toCreateResponse());
    }

    @PostMapping("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/like")
    public ResponseEntity<?> like(@CurrentUser User user,
                                  @PathVariable final long dramaId,
                                  @PathVariable final long episodeId,
                                  @PathVariable final long commentId) {
        dramaService.ensureExistsBy(dramaId);
        dramaEpisodeService.ensureExistsBy(episodeId);
        dramaEpisodeCommentService.ensureExistsBy(commentId);

        final DramaEpisodeCommentLike commentLike = dramaEpisodeCommentService.doLike(user, commentId);
        return ResponseEntity.ok(commentLike.toResponse());
    }

    @PostMapping("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/dislike")
    public ResponseEntity<?> dislike(@CurrentUser User user,
                                     @PathVariable final long dramaId,
                                     @PathVariable final long episodeId,
                                     @PathVariable final long commentId) {
        dramaService.ensureExistsBy(dramaId);
        dramaEpisodeService.ensureExistsBy(episodeId);
        dramaEpisodeCommentService.ensureExistsBy(commentId);

        final DramaEpisodeCommentLike commentLike = dramaEpisodeCommentService.doDislike(user, commentId);
        return ResponseEntity.ok(commentLike.toResponse());
    }
}
