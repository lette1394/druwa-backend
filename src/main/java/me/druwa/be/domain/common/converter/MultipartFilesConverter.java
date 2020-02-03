package me.druwa.be.domain.common.converter;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;
import me.druwa.be.domain.common.model.MultipartImage;
import me.druwa.be.domain.common.model.MultipartImages;

public class MultipartFilesConverter implements Converter<Map<String, MultipartFile>, MultipartImages> {
    @Override
    public MultipartImages convert(final Map<String, MultipartFile> source) {
        if (source.isEmpty()) {
            return new MultipartImages();
        }
        return new MultipartImages(source.values()
                                         .stream()
                                         .map(MultipartImage::new)
                                         .collect(Collectors.toSet()));

    }
}
