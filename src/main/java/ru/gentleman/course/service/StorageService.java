package ru.gentleman.course.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadImage(MultipartFile multipartFile);

    byte[] downloadImage(String fileName);

    void deleteImage(String fileName);
}
