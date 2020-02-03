package me.druwa.be.domain.common.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.drama.model.DramaImages;
import me.druwa.be.domain.drama_episode.model.DramaEpisode;
import me.druwa.be.domain.drama_episode.model.DramaEpisodeImages;

import static me.druwa.be.domain.drama.model.DramaImages.dramaImages;
import static me.druwa.be.domain.drama_episode.model.DramaEpisodeImages.dramaEpisodeImages;

@NoArgsConstructor
@AllArgsConstructor
public class MultipartImages {
    private Set<MultipartImage> images = new HashSet<>();

    public static MultipartImages dramaMultipartImages(final Map<String, MultipartFile> source) {
        if (source.isEmpty()) {
            return new MultipartImages();
        }
        return new MultipartImages(source.values()
                                         .stream()
                                         .map(MultipartImage::new)
                                         .collect(Collectors.toSet()));
    }

    public static MultipartImages dramaMultipartImages(final Set<MultipartImage> images) {
        return new MultipartImages(images);
    }

    public MultipartImages(final MultipartFile[] source) {
        this(Arrays.stream(source)
                   .map(MultipartImage::new)
                   .collect(Collectors.toSet()));
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }

    public DramaImages toDramaImages(final Drama drama) {
        return dramaImages(images.stream()
                                 .map(image -> image.toDramaImage(drama))
                                 .collect(Collectors.toSet()));
    }

    public DramaEpisodeImages toDramaEpisodeImages(final DramaEpisode dramaEpisode) {
        return dramaEpisodeImages(images.stream()
                                        .map(image -> image.toDramaEpisodeImage(dramaEpisode))
                                        .collect(Collectors.toSet()));
    }

    public Stream<MultipartImage> stream() {
        return images.stream();
    }
}
