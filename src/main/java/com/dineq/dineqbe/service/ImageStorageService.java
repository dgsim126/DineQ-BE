package com.dineq.dineqbe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageStorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.image-base-url}")
    private String imageBaseUrl;

    public String saveImage(MultipartFile imageFile) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();
        String filename = UUID.randomUUID() + "_" + originalFilename;
        File dest = new File(uploadDir + filename);

        // 디렉토리 없으면 생성
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        imageFile.transferTo(dest);

        // 클라이언트에서 접근할 수 있는 경로 반환
        return imageBaseUrl + filename;
    }
}
