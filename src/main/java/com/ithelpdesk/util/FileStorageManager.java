package com.ithelpdesk.util;

public class FileStorageManager {
    private final FileStorage fileStorage;

    public FileStorageManager(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    public String saveFile(byte[] fileData, String fileName) throws Exception {
        return fileStorage.saveFile(fileName, fileData); // Return the file path or key
    }

    public byte[] retrieveFile(String fileName) throws Exception {
        return fileStorage.retrieveFile(fileName);
    }

    public void deleteFile(String fileName) throws Exception {
        fileStorage.deleteFile(fileName);
    }
}
