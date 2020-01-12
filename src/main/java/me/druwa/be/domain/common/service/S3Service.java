package me.druwa.be.domain.common.service;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

@Service
public class S3Service {

    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private AmazonS3 s3;

    @PostConstruct
    private void onConstruct() {
        s3 = AmazonS3ClientBuilder
                     .standard()
                     .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                     .withRegion(Regions.AP_NORTHEAST_2)
                     .build();
    }

    @SneakyThrows
    public String put(final @NotNull MultipartFile multipartFile) {
        final String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException();
        }
        final String[] split = originalFilename.split("\\.");
        if (split.length == 0) {
            throw new RuntimeException();
        }
        final String ext = split[split.length - 1];
        final File tempFile = createTempFile(ext);

        multipartFile.transferTo(tempFile);
        return put(tempFile);
    }

    public String put(final File file) {
        final String key = key(file);
        final AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        putObjectRequest.withAccessControlList(accessControlList);

        s3.putObject(putObjectRequest);
        return key;
    }

    public S3Object get(final String key) {
        return s3.getObject(bucketName, key);
    }

    private String key(final File file) {
        final long random = ThreadLocalRandom.current().nextLong(1000000);
        return String.format("%s-%s-%s", DateTime.now().getMillis(), random, file.getName());
    }

    private AWSCredentials credentials() {
        return new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }

    @SneakyThrows
    public static File createTempFile(final String ext) {
        final String tempFilePrefix = String.format("%s-%s",
                                                    DateTime.now().getMillis(),
                                                    ThreadLocalRandom.current().nextLong(1000000));
        final File tempFile = File.createTempFile(tempFilePrefix, "." + ext);
        tempFile.deleteOnExit();
        return tempFile;
    }
}
