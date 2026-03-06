package ru.gentleman.course.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.gentleman.common.exception.StorageException;
import ru.gentleman.course.prop.StorageProperties;
import ru.gentleman.course.service.StorageService;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultStorageService implements StorageService {

    private final StorageProperties storageProperties;

    private final S3Client s3Client;

    @Override
    public String uploadImage(MultipartFile image) {
        log.info("uploadImage");
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        try {
            this.s3Client.putObject(PutObjectRequest.builder()
                            .bucket(this.storageProperties.getBucketName())
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(image.getBytes()));
        } catch (IOException e) {
            log.error("uploadImage error ", e);
            throwStorageException(e);
        }
        return fileName;
    }

    @Override
    @Cacheable(value = "image", key = "#fileName")
    public byte[] downloadImage(String fileName) {
        log.info("downloadFile {}", fileName);
        ResponseBytes<GetObjectResponse> objectAsBytes =
                this.s3Client.getObjectAsBytes(GetObjectRequest.builder()
                        .bucket(this.storageProperties.getBucketName())
                        .key(fileName)
                        .build());
        return objectAsBytes.asByteArray();
    }

    @Override
    @CacheEvict(value = "image", key = "#fileName")
    public void deleteImage(String fileName) {
        log.info("deleteFile {}", fileName);
        this.s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(this.storageProperties.getBucketName())
                .key(fileName)
                .build());
    }

    private void throwStorageException(IOException e){
        log.error("uploadImages exception {}", e.getMessage());
        throw new StorageException(e);
    }
}
