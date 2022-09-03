package com.muravskyi.peopledbweb.data;

import com.muravskyi.peopledbweb.exception.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileStorageRepository {

    @Value("${STORAGE_FOLDER}")
    private String storageFolder;

    public void save(String originalFilename, InputStream inputStream) throws IOException {
        try {
            Path filePath = Path.of(storageFolder).resolve(originalFilename).normalize();
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException(e);
        }
    }

    public Resource findByName(String fileName) {
        try {
            Path filePath = Path.of(storageFolder).resolve(fileName).normalize();
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

}
