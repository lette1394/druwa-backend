package me.druwa.be.domain.drama.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.db.Image;
import me.druwa.be.domain.common.model.Mergeable;

@Embeddable
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DramaImages implements Mergeable<DramaImages> {

    // FIXME: equals hashcode가 똑같아서 덮어쓰기 기능이 동작하지 않는다.
    //  db에는 잘 저장되고, java set의 문제임. 디비에 versioning을 둬야 할 듯.
    @OneToMany(mappedBy = "drama")
    private Set<DramaImage> dramaImages;

    public static DramaImages dramaImages() {
        return dramaImages(new HashSet<>());
    }

    public static DramaImages dramaImages(final Set<DramaImage> dramaImages) {
        return new DramaImages(dramaImages);
    }

    public static DramaImages dramaImages(final List<DramaImage> dramaImages) {
        return dramaImages(Sets.newHashSet(dramaImages));
    }

    public Stream<DramaImage> stream() {
        return dramaImages.stream();
    }

    public Set<Image.View.Read.Response> toResponse() {
        return this.dramaImages.stream()
                               .map(DramaImage::toResponse)
                               .collect(Collectors.toSet());
    }

    @Override
    public DramaImages merge(final DramaImages other) {
        this.dramaImages.addAll(other.dramaImages);
        return this;
    }

    public Set<DramaImage> toRaw() {
        return dramaImages;
    }
}
