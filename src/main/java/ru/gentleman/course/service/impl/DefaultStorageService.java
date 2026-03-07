package ru.gentleman.course.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.gentleman.common.exception.StorageException;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.course.prop.StorageProperties;
import ru.gentleman.course.service.StorageService;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultStorageService implements StorageService {

    private final StorageProperties storageProperties;

    private final S3Client s3Client;

    @Override
    public String uploadImage(MultipartFile image) {
        log.info("uploadDocument");
        String fileName = System.currentTimeMillis() + "_" + "document.pdf";
        try {
            this.s3Client.putObject(PutObjectRequest.builder()
                            .bucket(this.storageProperties.getBucketName())
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(image.getBytes()));
        } catch (S3Exception e) {
            log.error("S3 Service Error: [Code: {}] {}", e.awsErrorDetails().errorCode(), e.getMessage());
            throw new StorageException(e);
        }  catch (Exception e) {
            log.error("Unexpected error during document upload to S3", e);
            throw new StorageException(e);
        }
        return fileName;
    }

    @Override
    @Cacheable(value = "image", key = "#link")
    public byte[] downloadImage(String link) {
        log.info("downloadDocument {}", link);
        try {
            ResponseBytes<GetObjectResponse> objectAsBytes =
                    this.s3Client.getObjectAsBytes(GetObjectRequest.builder()
                            .bucket(this.storageProperties.getBucketName())
                            .key(link)
                            .build());
            return objectAsBytes.asByteArray();
        } catch (NoSuchKeyException e){
            throw ExceptionUtils.notFound("error.storage.not_found", link);
        } catch (S3Exception e) {
            log.error("S3 Service Error: [Code: {}] {}", e.awsErrorDetails().errorCode(), e.getMessage());
            throw new StorageException(e);
        } catch (Exception e) {
            log.error("Unexpected error during document upload to S3", e);
            throw new StorageException(e);
        }
    }

    @Override
    @CacheEvict(value = "image", key = "#link")
    public void deleteImage(String link) {
        log.info("deleteDocument {}", link);
        try {
            this.s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(this.storageProperties.getBucketName())
                    .key(link)
                    .build());
        } catch (S3Exception e) {
            log.error("S3 Service Error: [Code: {}] {}", e.awsErrorDetails().errorCode(), e.getMessage());
            throw new StorageException(e);
        } catch (Exception e) {
            log.error("Unexpected error during document upload to S3", e);
            throw new StorageException(e);
        }
    }
}
