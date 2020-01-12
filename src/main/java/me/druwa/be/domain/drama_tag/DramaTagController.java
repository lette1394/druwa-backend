package me.druwa.be.domain.drama_tag;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.service.DramaService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaTagController {
    private final DramaService dramaService;
    private final DramaTagService dramaTagService;

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

    @GetMapping("/tags")
    public ResponseEntity<?> find() {
        final DramaTags dramaTags = dramaTagService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                             .body(dramaTags.toResponse());
    }
}
