package me.druwa.be.domain.drama_review;

import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode(of = "dramaReviews")
@ToString(of = "dramaReviews")
@NoArgsConstructor
@AllArgsConstructor
public class DramaReviews {

    @ManyToMany
    private Set<DramaReview> dramaReviews;
}
