package me.druwa.be.domain.drama.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.model.DramaImage;
import me.druwa.be.domain.drama.model.DramaPopularType;
import me.druwa.be.domain.drama.model.DramaSearchQuery;
import me.druwa.be.domain.drama.model.DramaSearchStrings;
import me.druwa.be.domain.drama.model.Dramas;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.drama_tag.DramaTag;
import me.druwa.be.domain.drama_tag.DramaTagSearchStrings;
import me.druwa.be.domain.user.annotation.AllowPublicAccess;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;
import org.apache.commons.lang3.StringUtils;

import static me.druwa.be.domain.common.model.MultipartImages.dramaMultipartImages;

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

    @AllowPublicAccess
    @GetMapping("/dramas/populars")
    public ResponseEntity<?> populars(
            @RequestParam(name = "from") final LocalDateTime from,
            @RequestParam(name = "to") final LocalDateTime to,
            @RequestParam(name = "p_type") final DramaPopularType dramaPopularType,
            @RequestParam(name = "tag",
                          required = false,
                          defaultValue = StringUtils.EMPTY) final DramaTagSearchStrings tags,
            @RequestParam(name = "count") final Long limit) {
        final Dramas dramas = dramaService.findPopularsBuilder()
                                          .from(from)
                                          .to(to)
                                          .tags(tags.toTags())
                                          .dramaPopularType(dramaPopularType)
                                          .limit(limit)
                                          .execute();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(dramas.toSearchResponse());
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}")
    public ResponseEntity<?> find(@Valid
                                  @PathVariable Long dramaId) {
        dramaService.ensureExistsBy(dramaId);
        final Drama drama = dramaService.findByDramaId(dramaId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toReadResponse());
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/related")
    public ResponseEntity<?> related(@Valid
                                     @PathVariable Long dramaId) {
        dramaService.ensureExistsBy(dramaId);
        final Dramas dramas = dramaService.findRelatedDramaById(dramaId, 10L);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(dramas.toReadResponse());
    }

    @PatchMapping("/dramas/{dramaId}")
    public ResponseEntity<?> update(@PathVariable final Long dramaId,
                                    @Valid
                                    @RequestBody Drama.View.Patch.Request body,
                                    @CurrentUser User user) {
        final Drama dramaBefore = dramaService.findByDramaId(dramaId);
        final Drama drama = dramaService.update(dramaBefore, user, body);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toCreateResponse());
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/images")
    public ResponseEntity<?> listImages(@PathVariable final Long dramaId) {
        final Drama drama = dramaService.findByDramaId(dramaId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toImageOnlyResponse());
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/images/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable final Long dramaId,
                                      @PathVariable final String imageName) {
        final DramaImage dramaImage = dramaService.findImage(dramaId, imageName);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(dramaImage.toResponse());
    }

    @PostMapping(value = "/dramas/{dramaId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createImages(@PathVariable final Long dramaId,
                                          @Valid @NonNull @RequestParam Map<String, MultipartFile> images) {

        final Drama drama = dramaService.createDramaImage(dramaId, dramaMultipartImages(images));

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toImageOnlyResponse());
    }

    @PatchMapping("/dramas/{dramaId}/like")
    public ResponseEntity<?> like(@Valid
                                  @PathVariable final Long dramaId,
                                  @CurrentUser User user) {
        dramaService.ensureExistsBy(dramaId);
        final Drama drama = dramaService.doLike(dramaId, user);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toLikeResponse(user));
    }

    @PatchMapping("/dramas/{dramaId}/dislike")
    public ResponseEntity<?> dislike(@Valid
                                     @PathVariable final Long dramaId,
                                     @CurrentUser User user) {
        dramaService.ensureExistsBy(dramaId);
        final Drama drama = dramaService.doDislike(dramaId, user);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(drama.toLikeResponse(user));
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

    // TODO: 문서화
    @AllowPublicAccess
    @GetMapping("/search")
    public ResponseEntity<?> find(
            @RequestParam(name = "tag",
                          required = false,
                          defaultValue = StringUtils.EMPTY) final DramaTagSearchStrings tags,
            @RequestParam(name = "q") final DramaSearchStrings dramas) {

        final DramaSearchQuery searchQuery = DramaSearchQuery.builder()
                                                             .dramaSearchStrings(dramas)
                                                             .dramaTagSearchStrings(tags)
                                                             .build();

        final Dramas search = dramaService.search(searchQuery);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(search.toSearchResponse());
    }
}
