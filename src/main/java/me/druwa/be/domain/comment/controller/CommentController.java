package me.druwa.be.domain.comment.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import me.druwa.be.domain.comment.service.CommentService;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.comment.model.Comment;
import me.druwa.be.domain.user.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/test/123")
    public ResponseEntity<?> create(@Valid @RequestBody Comment.View.Create.Request body, @CurrentUser User user) {
        log.info("useruser123, {}", user);
        final Comment comment = commentService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(comment.toCreateResponse());
    }

    @PostMapping("/aa/{prevCommentId}")
    public ResponseEntity<?> append(@PathVariable Long prevCommentId, @Valid @RequestBody Comment.View.Create body) {
        return null;
    }
}
