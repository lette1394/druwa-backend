package me.druwa.be.domain.common.converter;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;
import me.druwa.be.domain.drama.model.DramaMultipartImage;
import me.druwa.be.domain.drama.model.DramaMultipartImages;

public class MultipartFilesConverter implements Converter<Map<String, MultipartFile>, DramaMultipartImages> {
    @Override
    public DramaMultipartImages convert(final Map<String, MultipartFile> source) {
        if (source.isEmpty()) {
            return new DramaMultipartImages();
        }
        return new DramaMultipartImages(source.values()
                                              .stream()
                                              .map(DramaMultipartImage::new)
                                              .collect(Collectors.toSet()));

    }
}
