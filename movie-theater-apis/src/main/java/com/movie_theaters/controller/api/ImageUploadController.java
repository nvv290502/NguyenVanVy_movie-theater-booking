package com.movie_theaters.controller.api;

import com.movie_theaters.service.impl.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = imageUploadService.uploadImage(file);
            return ResponseEntity.ok(imageUrl); // Trả về URL của ảnh đã tải lên
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi khi tải ảnh lên Cloudinary");
        }
    }
}
