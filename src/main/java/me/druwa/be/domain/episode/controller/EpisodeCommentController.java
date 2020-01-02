package me.druwa.be.domain.episode.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.episode.model.EpisodeComment;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.episode.model.EpisodeCommentLike;
import me.druwa.be.domain.episode.service.EpisodeCommentService;
import me.druwa.be.domain.episode.service.EpisodeService;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dramas")
public class EpisodeCommentController {
    private final DramaService dramaService;
    private final EpisodeService episodeService;
    private final EpisodeCommentService episodeCommentService;


    @PostMapping("/{dramaId}/episodes/{episodesId}/comments")
    public ResponseEntity<?> create(@Valid
                                    @RequestBody EpisodeComment.View.Create.Request body,
                                    @CurrentUser User user,
                                    @PathVariable final long dramaId,
                                    @PathVariable final long episodesId) {


        final EpisodeComment episodeComment = episodeCommentService.create(body, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(episodeComment.toCreateResponse());
    }

    @PostMapping("/{dramaId}/episodes/{episodeId}/comments/{commentId}/like")
    public ResponseEntity<?> like(@CurrentUser User user,
                                  @PathVariable final long dramaId,
                                  @PathVariable final long episodeId,
                                  @PathVariable final long commentId) {
        dramaService.ensureExistsBy(dramaId);
        episodeService.ensureExistsBy(episodeId);
        episodeCommentService.ensureExistsBy(commentId);

        final EpisodeCommentLike commentLike = episodeCommentService.doLike(user, commentId);
        return ResponseEntity.ok(commentLike.toResponse());
    }

    @PostMapping("/{dramaId}/episodes/{episodeId}/comments/{commentId}/dislike")
    public ResponseEntity<?> dislike(@CurrentUser User user,
                                  @PathVariable final long dramaId,
                                  @PathVariable final long episodeId,
                                  @PathVariable final long commentId) {
        dramaService.ensureExistsBy(dramaId);
        episodeService.ensureExistsBy(episodeId);
        episodeCommentService.ensureExistsBy(commentId);

        final EpisodeCommentLike commentLike = episodeCommentService.doDislike(user, commentId);
        return ResponseEntity.ok(commentLike.toResponse());
    }

    @PostMapping("/aa/{prevCommentId}")
    public ResponseEntity<?> append(@PathVariable Long prevCommentId,
                                    @Valid @RequestBody EpisodeComment.View.Create body) {
        return null;
    }
}
