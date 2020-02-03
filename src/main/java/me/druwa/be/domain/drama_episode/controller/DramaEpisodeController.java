package me.druwa.be.domain.drama_episode.controller;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.domain.drama_episode.model.DramaEpisodes;
import me.druwa.be.domain.drama_episode.service.DramaEpisodeService;
import me.druwa.be.domain.user.annotation.AllowPublicAccess;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

import static me.druwa.be.domain.common.model.MultipartImages.dramaMultipartImages;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaEpisodeController {

    private final DramaService dramaService;
    private final DramaEpisodeService dramaEpisodeService;

    @PostMapping("/dramas/{dramaId}/episodes")
    public ResponseEntity<?> create(@PathVariable final Long dramaId,
                                    @Valid
                                    @RequestBody final DramaEpisode.View.Create.Request body,
                                    @CurrentUser final User user) {
        final Drama drama = dramaService.findByDramaId(dramaId);
        final DramaEpisode dramaEpisode = dramaEpisodeService.create(user, drama, body);

        return ResponseEntity.ok(dramaEpisode.toCreateResponse());
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/episodes")
    public ResponseEntity<?> list(@PathVariable final Long dramaId,
                                  @CurrentUser final User user) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisodes episodes = dramaEpisodeService.findByDramaId(dramaId);
        return ResponseEntity.ok(episodes.toReadResponse());
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/episodes/{episodeId}")
    public ResponseEntity<?> find(@PathVariable final Long dramaId,
                                  @PathVariable final Long episodeId) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisode dramaEpisode = dramaEpisodeService.findByEpisodeId(episodeId);

        return ResponseEntity.ok(dramaEpisode.toReadResponse());
    }

    @AllowPublicAccess
    @GetMapping(value = "/dramas/{dramaId}/episodes/{episodeId}/images",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> listImages(@PathVariable final Long dramaId,
                                        @PathVariable final Long episodeId) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisode dramaEpisode = dramaEpisodeService.findByEpisodeId(episodeId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(dramaEpisode.toImageOnlyResponse());
    }

    @PostMapping(value = "/dramas/{dramaId}/episodes/{episodeId}/images",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createImages(@PathVariable final Long dramaId,
                                          @PathVariable final Long episodeId,
                                          @Valid @NonNull @RequestParam Map<String, MultipartFile> images) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisode dramaEpisode = dramaEpisodeService.createDramaImage(episodeId, dramaMultipartImages(images));

        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }
}
