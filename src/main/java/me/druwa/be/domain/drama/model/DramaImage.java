package me.druwa.be.domain.drama.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DramaImage {
    private static final String S3_URL_BASE_ENDPOINT = "https://druwa-repository-test.s3.ap-northeast-2.amazonaws.com";

    @Column
    private String imageKey;

    public String toS3Url() {
        return String.format("%s/%s", S3_URL_BASE_ENDPOINT, imageKey);
    }

}
