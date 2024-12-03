package com.ithelpdesk.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LocalFileStorage implements FileStorage {
    private final String storagePath;

    public LocalFileStorage(String storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public String saveFile(String fileName, byte[] data) {
        try {
            java.nio.file.Path filePath = Paths.get(storagePath, fileName);
            Files.write(filePath, data);
            return filePath.toString(); // Return the saved file's path
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file locally: " + fileName, e);
        }
    }


    @Override
    public byte[] retrieveFile(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(storagePath, fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve file locally: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        File file = new File(Paths.get(storagePath, fileName).toString());
        if (!file.delete()) {
            throw new RuntimeException("Failed to delete file locally: " + fileName);
        }
    }
}
