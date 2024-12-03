package com.ithelpdesk.util;

public interface FileStorage {
    String saveFile(String fileName, byte[] data); // Return the file path or key
    byte[] retrieveFile(String fileName);
    void deleteFile(String fileName);
}

