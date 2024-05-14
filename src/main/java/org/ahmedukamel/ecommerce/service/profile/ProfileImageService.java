package org.ahmedukamel.ecommerce.service.profile;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileImageService {
    byte[] getImage(String imageName) throws IOException;

    ApiResponse uploadImage(MultipartFile image) throws IOException;

    ApiResponse deleteImage() throws IOException;
}