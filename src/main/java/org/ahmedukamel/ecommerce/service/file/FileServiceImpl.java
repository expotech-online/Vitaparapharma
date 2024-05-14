package org.ahmedukamel.ecommerce.service.file;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String save(MultipartFile file, String directoryUri) throws IOException {
        Path directoryPath = Path.of(directoryUri);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Supplier<String> generateName = () -> String.format("%s.%s",
                UUID.randomUUID(),
                FilenameUtils.getExtension(file.getOriginalFilename()));

        String fileName = generateName.get();
        Path filePath = directoryPath.resolve(fileName);

        while (Files.exists(filePath)) {
            fileName = generateName.get();
            filePath = directoryPath.resolve(fileName);
        }

        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

    @Override
    public byte[] get(String fileName, String directoryUri) throws IOException {
        Path filePath = Path.of(directoryUri).resolve(fileName);
        return Files.exists(filePath) ? Files.readAllBytes(filePath) : null;
    }

    @Override
    public void delete(String fileName, String directoryUri) throws IOException {
        Path filePath = Path.of(directoryUri).resolve(fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}