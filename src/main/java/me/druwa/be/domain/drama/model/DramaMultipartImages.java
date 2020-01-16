package me.druwa.be.domain.drama.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static me.druwa.be.domain.drama.model.DramaImages.dramaImages;

@NoArgsConstructor
@AllArgsConstructor
public class DramaMultipartImages {
    private Set<DramaMultipartImage> images = new HashSet<>();

    public static DramaMultipartImages dramaMultipartImages(final Map<String, MultipartFile> source) {
        if (source.isEmpty()) {
            return new DramaMultipartImages();
        }
        return new DramaMultipartImages(source.values()
                                              .stream()
                                              .map(DramaMultipartImage::new)
                                              .collect(Collectors.toSet()));
    }

    public static DramaMultipartImages dramaMultipartImages(final Set<DramaMultipartImage> images) {
        return new DramaMultipartImages(images);
    }

    public DramaMultipartImages(final MultipartFile[] source) {
        this(Arrays.stream(source)
                   .map(DramaMultipartImage::new)
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

    public Stream<DramaMultipartImage> stream() {
        return images.stream();
    }
}
