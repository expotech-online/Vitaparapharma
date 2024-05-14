package org.ahmedukamel.ecommerce.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageDirectoryUtils {
    //    public static final String DIRECTORY_PATH = "/home/ecommerce/ecommerce-stored-files/test";
//    public static final String DIRECTORY_PATH = "/home/ecommerce/ecommerce-stored-files";
    public static final String DIRECTORY_PATH = "/app/images/product";
    //    public static final String PROFILE_DIRECTORY_PATH = "/home/ahmedukamel/app/images/Vita";
    public static final String PROFILE_DIRECTORY_PATH = "/app/images/profile";
    public static final String API_URL = "/api/v1/public/view/";

    public static String saveImage(MultipartFile file) throws IOException {
//        String imageName = generateName(file.getOriginalFilename());

        String imageName = "%s.%s".formatted(UUID.randomUUID().toString(), FilenameUtils.getExtension(file.getOriginalFilename()));
        if (!Files.exists(Path.of(DIRECTORY_PATH))) {
            Files.createDirectories(Path.of(DIRECTORY_PATH));
        }
        Path path = getImagePath(imageName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return imageName;
    }

    public static void deleteImage(String imageUrl) {
        String imageName = ImageDirectoryUtils.getImageName(imageUrl);
        new File(ImageDirectoryUtils.getImagePath(imageName).toString()).delete();
    }

    public static String getImageUrl(String imageName) {
        return "https://api.vitaparapharma.com" + API_URL + imageName;
    }

    public static Path getImagePath(String imageName) {
        return Paths.get(DIRECTORY_PATH + File.separator + imageName);
    }

    public static String generateName(String name) {
        return UUID.randomUUID() + "_" + name;
    }

    public static String getImageName(String imageUrl) {
        String[] parts = imageUrl.split("/");
        return parts[parts.length - 1];
    }
}
