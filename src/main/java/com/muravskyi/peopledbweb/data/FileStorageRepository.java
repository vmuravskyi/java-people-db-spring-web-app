package com.muravskyi.peopledbweb.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
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
        }
    }

}
