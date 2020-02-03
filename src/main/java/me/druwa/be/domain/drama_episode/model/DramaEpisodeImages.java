package me.druwa.be.domain.drama_episode.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.db.Image;
import me.druwa.be.domain.common.model.Mergeable;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DramaEpisodeImages implements Mergeable<DramaEpisodeImages> {

    @OneToMany(mappedBy = "dramaEpisode", cascade = CascadeType.PERSIST)
    private Set<DramaEpisodeImage> dramaEpisodeImages;

    public static DramaEpisodeImages dramaEpisodeImages() {
        return dramaEpisodeImages(new HashSet<>());
    }

    public static DramaEpisodeImages dramaEpisodeImages(final Collection<DramaEpisodeImage> dramaEpisodeImages) {
        return new DramaEpisodeImages(Sets.newHashSet(dramaEpisodeImages));
    }

    public Set<Image.View.Read.Response> toResponse() {
        return this.dramaEpisodeImages.stream()
                                      .map(DramaEpisodeImage::toResponse)
                                      .collect(Collectors.toSet());
    }

    @Override
    public DramaEpisodeImages merge(final DramaEpisodeImages other) {
        Set<DramaEpisodeImage> images = Sets.newHashSet();
        images.addAll(other.dramaEpisodeImages);
        images.addAll(this.dramaEpisodeImages);
        this.dramaEpisodeImages = images;
        return this;
    }

    public Set<DramaEpisodeImage> toRaw() {
        return dramaEpisodeImages;
    }
}
