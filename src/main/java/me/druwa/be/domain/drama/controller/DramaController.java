package me.druwa.be.domain.drama.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.activation.MimeTypeParameterList;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.drama_episode_comment.model.Like;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaController {

    private final DramaService dramaService;

    @PostMapping("/dramas")
    public ResponseEntity<?> create(@Valid
                                    @RequestBody Drama.View.Create.Request body,
                                    @CurrentUser User user) {
        final Drama drama = dramaService.create(user, body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(drama.toCreateResponse());
    }

    @PostMapping(value = "/dramas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMultipart(@Valid Drama.View.Create.MultipartRequest body,
                                             @CurrentUser User user) {

        final Drama drama = dramaService.createMultipart(user, body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(drama.toCreateResponse());
    }

    @GetMapping("/dramas/{dramaId}")
    public ResponseEntity<?> find(@Valid
                                  @PathVariable final long dramaId) {
        dramaService.ensureExistsBy(dramaId);
        final Drama drama = dramaService.findByDramaId(dramaId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toReadResponse());
    }

    @PatchMapping("/dramas/{dramaId}")
    public ResponseEntity<?> update(@PathVariable final long dramaId,
                                    @Valid
                                    @RequestBody Drama.View.Patch.Request body,
                                    @CurrentUser User user) {
        final Drama dramaBefore = dramaService.findByDramaId(dramaId);
        final Drama drama = dramaService.update(dramaBefore, user, body);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toCreateResponse());
    }

    @PostMapping("/dramas/{dramaId}/like")
    public ResponseEntity<?> like(@Valid
                                  @PathVariable final long dramaId,
                                  @CurrentUser User user) {
        dramaService.ensureExistsBy(dramaId);
        final Like like = dramaService.doLike(dramaId, user);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(like.toResponse());
    }

    @PostMapping("/dramas/{dramaId}/dislike")
    public ResponseEntity<?> dislike(@Valid
                                     @PathVariable final long dramaId,
                                     @CurrentUser User user) {
        dramaService.ensureExistsBy(dramaId);
        final Like like = dramaService.doDislike(dramaId, user);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(like.toResponse());
    }
}
