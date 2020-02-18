package me.druwa.be.domain.drama_review;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import me.druwa.be.domain.user.annotation.AllowPublicAccess;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.global.exception.DruwaException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DramaReviewController {
    private final DramaService dramaService;
    private final DramaReviewService dramaReviewService;

    @PostMapping("/dramas/{dramaId}/reviews")
    public ResponseEntity<?> create(@CurrentUser final User user,
                                    @PathVariable final Long dramaId,
                                    @Valid @RequestBody final DramaReview.View.Create.Request request) {

        final Drama drama = dramaService.findByDramaId(dramaId);
        final DramaReview dramaReview = request.toPartialDramaReview()
                                               .writtenBy(user)
                                               .drama(drama)
                                               .build();
        final DramaReview saved = dramaReviewService.save(dramaReview);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/reviews")
    public ResponseEntity<?> list(@CurrentUser final User user,
                                  @PathVariable final Long dramaId,
                                  final Pageable pageable) {

        dramaService.ensureExistsBy(dramaId);
        final DramaReviews reviews = dramaReviewService.findAll(dramaId, pageable);

        return ResponseEntity.ok(reviews.toReadResponse(user));
    }

    @AllowPublicAccess
    @GetMapping("/dramas/{dramaId}/reviews/{reviewId}")
    public ResponseEntity<?> get(@CurrentUser final User user,
                                 @PathVariable final Long dramaId,
                                 @PathVariable final Long reviewId) {

        dramaService.ensureExistsBy(dramaId);
        DramaReview dramaReview = dramaReviewService.find(reviewId);

        return ResponseEntity.ok(dramaReview.toReadResponse(user));
    }
}
