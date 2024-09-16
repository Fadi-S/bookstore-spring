package dev.fadisarwat.bookstore.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String saveImageToStorage(String uploadDirectory, MultipartFile imageFile) throws IOException;
    byte[] getImage(String imageDirectory, String imageName) throws IOException;
    Boolean deleteImage(String imageDirectory, String imageName) throws IOException;
}
