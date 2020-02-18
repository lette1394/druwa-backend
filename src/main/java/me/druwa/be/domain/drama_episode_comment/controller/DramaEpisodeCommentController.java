package me.druwa.be.domain.drama_episode_comment.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.model.LikeOrDislike;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.domain.drama_episode.service.DramaEpisodeService;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComment;
import me.druwa.be.domain.drama_episode_comment.model.DramaEpisodeComments;
import me.druwa.be.domain.drama_episode_comment.service.DramaEpisodeCommentService;
import me.druwa.be.domain.user.annotation.AllowPublicAccess;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaEpisodeCommentController {
    private final DramaService dramaService;
    private final DramaEpisodeService dramaEpisodeService;
    private final DramaEpisodeCommentService dramaEpisodeCommentService;

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/episodes/{episodeId}/comments")
    public ResponseEntity<?> list(@CurrentUser final User user,
                                  @PathVariable final Long dramaId,
                                  @PathVariable final Long episodeId) {
        dramaService.ensureExistsBy(dramaId);
        dramaEpisodeService.ensureExistsBy(episodeId);

        final DramaEpisodeComments list = dramaEpisodeCommentService.list(episodeId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(list.toResponse(user));
    }

    @PostMapping("/dramas/{dramaId}/episodes/{episodeId}/comments")
    public ResponseEntity<?> create(@Valid
                                    @RequestBody final DramaEpisodeComment.View.Create.Request body,
                                    @CurrentUser final User user,
                                    @PathVariable final Long dramaId,
                                    @PathVariable final Long episodeId) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisode episode = dramaEpisodeService.findByEpisodeId(episodeId);
        final DramaEpisodeComment comment = body.toPartialDramaEpisodeComment()
                                                .dramaEpisode(episode)
                                                .writtenBy(user)
                                                .build();

        final DramaEpisodeComment dramaEpisodeComment = dramaEpisodeCommentService.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(dramaEpisodeComment.toCreateResponse());
    }

    @PatchMapping("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}")
    public ResponseEntity<?> edit(@CurrentUser final User user,
                                  @Valid
                                  @RequestBody final DramaEpisodeComment.View.Update.Request body,
                                  @PathVariable final Long dramaId,
                                  @PathVariable final Long episodeId,
                                  @PathVariable final Long commentId) {
        dramaService.ensureExistsBy(dramaId);
        dramaEpisodeService.ensureExistsBy(episodeId);

        final DramaEpisodeComment comment = dramaEpisodeCommentService.findBy(commentId);
        final DramaEpisodeComment merged = comment.merge(body.toPartialDramaEpisodeComment());

        dramaEpisodeCommentService.save(merged);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/like")
    public ResponseEntity<?> like(@CurrentUser final User user,
                                  @PathVariable final Long dramaId,
                                  @PathVariable final Long episodeId,
                                  @PathVariable final Long commentId) {
        dramaService.ensureExistsBy(dramaId);
        dramaEpisodeService.ensureExistsBy(episodeId);
        dramaEpisodeCommentService.ensureExistsBy(commentId);

        final LikeOrDislike commentLikeOrDislike = dramaEpisodeCommentService.doLike(user, commentId);
        return ResponseEntity.ok(commentLikeOrDislike.toResponse(user));
    }

    @PatchMapping("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}/dislike")
    public ResponseEntity<?> dislike(@CurrentUser final User user,
                                     @PathVariable final Long dramaId,
                                     @PathVariable final Long episodeId,
                                     @PathVariable final Long commentId) {
        dramaService.ensureExistsBy(dramaId);
        dramaEpisodeService.ensureExistsBy(episodeId);
        dramaEpisodeCommentService.ensureExistsBy(commentId);

        final LikeOrDislike commentLikeOrDislike = dramaEpisodeCommentService.doDislike(user, commentId);
        return ResponseEntity.ok(commentLikeOrDislike.toResponse(user));
    }

    @PostMapping("/dramas/{dramaId}/episodes/{episodeId}/comments/{commentId}")
    public ResponseEntity<?> append(@Valid
                                    @RequestBody final DramaEpisodeComment.View.Create.Request body,
                                    @CurrentUser final User user,
                                    @PathVariable final Long dramaId,
                                    @PathVariable final Long episodeId,
                                    @PathVariable final Long commentId) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisode dramaEpisode = dramaEpisodeService.findByEpisodeId(episodeId);
        final DramaEpisodeComment prev = dramaEpisodeCommentService.findBy(commentId);
        final DramaEpisodeComment partialComment = body.toPartialDramaEpisodeComment()
                                                       .prev(prev)
                                                       .dramaEpisode(dramaEpisode)
                                                       .writtenBy(user)
                                                       .depth(1L)
                                                       .build();

        dramaEpisodeCommentService.save(partialComment);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }
}
