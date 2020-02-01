package me.druwa.be.domain.drama_episode.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.service.DramaService;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.domain.drama_episode.model.DramaEpisodes;
import me.druwa.be.domain.drama_episode.service.DramaEpisodeService;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaEpisodeController {

    private final DramaService dramaService;
    private final DramaEpisodeService dramaEpisodeService;

    @PostMapping("/dramas/{dramaId}/episodes")
    public ResponseEntity<?> create(@PathVariable Long dramaId,
                                    @Valid
                                    @RequestBody DramaEpisode.View.Create.Request body,
                                    @CurrentUser User user) {
        final Drama drama = dramaService.findByDramaId(dramaId);
        final DramaEpisode dramaEpisode = dramaEpisodeService.create(user, drama, body);

        return ResponseEntity.ok(dramaEpisode.toCreateResponse());
    }

    @GetMapping("/dramas/{dramaId}/episodes")
    public ResponseEntity<?> create(@PathVariable Long dramaId,
                                    @CurrentUser User user) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisodes episodes = dramaEpisodeService.findByDramaId(dramaId);
        return ResponseEntity.ok(episodes.toReadResponse());
    }

    @GetMapping("/dramas/{dramaId}/episodes/{episodeId}")
    public ResponseEntity<?> find(@PathVariable Long dramaId,
                                  @PathVariable Long episodeId) {
        dramaService.ensureExistsBy(dramaId);
        final DramaEpisode dramaEpisode = dramaEpisodeService.findByEpisodeId(episodeId);

        return ResponseEntity.ok(dramaEpisode.toReadResponse());
    }
}
