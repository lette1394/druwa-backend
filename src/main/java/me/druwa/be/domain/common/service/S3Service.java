package me.druwa.be.domain.common.service;

import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import me.druwa.be.domain.common.model.MultipartImage;
import me.druwa.be.domain.common.model.MultipartImages;

import static me.druwa.be.domain.common.model.MultipartImages.dramaMultipartImages;

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

    public MultipartImage put(final MultipartImage image) {
        final AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, image.key(), image.toFile());
        putObjectRequest.withAccessControlList(accessControlList);

        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.contentType());
        putObjectRequest.withMetadata(objectMetadata);

        s3.putObject(putObjectRequest);
        return image;
    }

    public MultipartImages put(final MultipartImages images) {
        return dramaMultipartImages(images.stream()
                                          .map(this::put)
                                          .collect(Collectors.toSet()));
    }

    public S3Object get(final String key) {
        return s3.getObject(bucketName, key);
    }

    private AWSCredentials credentials() {
        return new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }
}
