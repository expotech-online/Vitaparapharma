package org.ahmedukamel.ecommerce.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String save(MultipartFile file, String directoryPath) throws IOException;

    byte[] get(String fileName, String directoryPath) throws IOException;

    void delete(String fileName, String directoryPath) throws IOException;
}