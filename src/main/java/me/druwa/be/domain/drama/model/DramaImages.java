package me.druwa.be.domain.drama.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.db.Image;

@Embeddable
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DramaImages {

    @OneToMany(mappedBy = "drama")
    private Set<DramaImage> dramaImages;

    public static DramaImages dramaImages() {
        return dramaImages(new HashSet<>());
    }

    public static DramaImages dramaImages(final Set<DramaImage> dramaImages) {
        return new DramaImages(dramaImages);
    }

    public static DramaImages dramaImages(final String... dramaImages) {
        return dramaImages(Arrays.stream(dramaImages)
                                 .map(DramaImage::dramaImage)
                                 .collect(Collectors.toSet()));
    }

    public Set<Image.View.Read.Response> toResponse() {
        return this.dramaImages.stream()
                               .map(DramaImage::toResponse)
                               .collect(Collectors.toSet());
    }
}
