package me.druwa.be.domain.drama.controller;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.drama_episode_comment.model.Like;
import me.druwa.be.domain.drama_tag.DramaTag;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

import static me.druwa.be.domain.drama.model.DramaMultipartImages.dramaMultipartImages;

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

    @GetMapping("/dramas/{dramaId}/images")
    public ResponseEntity<?> listImages(@PathVariable final long dramaId) {
        final Drama drama = dramaService.findByDramaId(dramaId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toImageOnlyResponse());
    }

    @PostMapping(value = "/dramas/{dramaId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createImages(@PathVariable final long dramaId,
                                          @Valid @NonNull @RequestParam Map<String, MultipartFile> images) {

        final Drama drama = dramaService.createDramaImage(dramaId, dramaMultipartImages(images));

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toImageOnlyResponse());
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


    // TODO: drama create - update 와 합치기.
    //  왜 따로 만들었지??
    @RequestMapping(method = { RequestMethod.POST, RequestMethod.PATCH },
                    path = "/dramas/{dramaId}/tags")
    public ResponseEntity<?> createOrUpdate(@PathVariable final Long dramaId,
                                            @Valid
                                            @RequestBody final DramaTag.View.Create.Request body) {
        dramaService.ensureExistsBy(dramaId);
        dramaService.update(dramaId, body.toPartialTags());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }
}
