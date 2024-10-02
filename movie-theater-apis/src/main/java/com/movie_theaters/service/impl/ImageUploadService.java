package com.movie_theaters.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        if(file != null) {
            long startTime = System.currentTimeMillis();
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            long endTime = System.currentTimeMillis();
            System.out.println("Upload time: " + (endTime - startTime) + "ms");
            return uploadResult.get("url").toString(); // Trả về URL của ảnh đã tải lên
        }
        return "file is null";
    }
}
