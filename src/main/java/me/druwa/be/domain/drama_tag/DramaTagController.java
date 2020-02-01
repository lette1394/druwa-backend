package me.druwa.be.domain.drama_tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.druwa.be.domain.drama.service.DramaService;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaTagController {
    private final DramaService dramaService;
    private final DramaTagService dramaTagService;

    @GetMapping("/tags")
    public ResponseEntity<?> find(@RequestParam(name = "q",
                                                required = false,
                                                defaultValue = StringUtils.EMPTY)
                                  final DramaTagSearchStrings searchWords) {
        final DramaTags dramaTags = dramaTagService.findAll(searchWords);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(dramaTags.toResponse());
    }
}
