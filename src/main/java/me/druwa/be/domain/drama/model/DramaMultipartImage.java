package me.druwa.be.domain.drama.model;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.druwa.be.domain.common.db.ImageType;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;


@RequiredArgsConstructor
public class DramaMultipartImage {
    private final MultipartFile multipartFile;

    private String imageKey;

    @SneakyThrows
    public File toFile() {
        final File tempFile = File.createTempFile(key(), StringUtils.EMPTY);
        tempFile.deleteOnExit();
        multipartFile.transferTo(tempFile);

        return tempFile;
    }

    public DramaImage toDramaImage(final Drama drama) {
        return DramaImage.builder()
                         .drama(drama)
                         .imageKey(key())
                         .imageName(multipartFile.getName())
                         .imageType(ImageType.parse(extractExt(multipartFile)))
                         .build();
    }

    public String key() {
        if (StringUtils.isBlank(imageKey)) {
            return imageKey = String.format("%s-%s-%s.%s",
                                            multipartFile.getName(),
                                            DateTime.now().getMillis(),
                                            ThreadLocalRandom.current().nextLong(1000000),
                                            extractExt(multipartFile));
        }
        return imageKey;
    }

    public String contentType() {
        return multipartFile.getContentType();
    }

    private static String extractExt(final MultipartFile multipartFile) {
        final String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException();
        }
        final String[] split = originalFilename.split("\\.");
        if (split.length == 0) {
            throw new RuntimeException();
        }
        return split[split.length - 1];
    }
}
