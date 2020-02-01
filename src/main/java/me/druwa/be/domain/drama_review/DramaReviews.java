package me.druwa.be.domain.drama_review;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.user.model.User;

@Embeddable
@EqualsAndHashCode(of = "dramaReviews")
@ToString(of = "dramaReviews")
@NoArgsConstructor
@AllArgsConstructor
public class DramaReviews {

    @ManyToMany
    private Set<DramaReview> dramaReviews;

    public Set<DramaReview.View.Read.Response> toReadResponse(final User user) {
        return dramaReviews.stream()
                           .map(review -> review.toReadResponse(user))
                           .collect(Collectors.toSet());
    }
}
