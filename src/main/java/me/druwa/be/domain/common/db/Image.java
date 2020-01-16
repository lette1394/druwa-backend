package me.druwa.be.domain.common.db;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;

import static java.util.Objects.nonNull;

@MappedSuperclass
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "imageId", "imageName" })
public class Image {
    private static final String S3_URL_BASE_ENDPOINT = "https://druwa-repository-test.s3.ap-northeast-2.amazonaws.com";

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long imageId;

    @Column
    @NotBlank
    private String imageName;

    @Column
    @NotBlank
    private String imageKey;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong widthPixel;

    @Column
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong heightPixel;

    @Column
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @Embedded
    private Timestamp timestamp;

    @PrePersist
    public void onCreate() {
        timestamp = new Timestamp();
        timestamp.onCreate();
    }

    @PreUpdate
    public void onUpdate() {
        if (nonNull(timestamp)) {
            timestamp.onUpdate();
        }
    }

    public String toS3Url() {
        return String.format("%s/%s", S3_URL_BASE_ENDPOINT, imageKey);
    }

    public View.Read.Response toResponse() {
        return View.Read.Response.builder()
                                 .imageName(imageName)
                                 .imageUrl(toS3Url())
                                 .imageType(imageType)
                                 .widthPixel(widthPixel)
                                 .heightPixel(heightPixel)
                                 .timestamp(timestamp)
                                 .build();
    }

    public static class View {
        public static class Read {
            @Data
            @Builder
            public static class Response {
                private String imageName;
                private String imageUrl;
                private PositiveOrZeroLong widthPixel;
                private PositiveOrZeroLong heightPixel;
                private ImageType imageType;
                @JsonUnwrapped
                private Timestamp timestamp;
            }
        }
    }
}
