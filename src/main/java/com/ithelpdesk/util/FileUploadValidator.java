package com.ithelpdesk.util;

import java.util.Arrays;
import java.util.List;

public class FileUploadValidator {
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "application/pdf");
    private static final long MAX_FILE_SIZE_MB = 10;

    // Validate file type
    public static boolean isValidFileType(String mimeType) {
        return ALLOWED_FILE_TYPES.contains(mimeType);
    }

    // Validate file size
    public static boolean isValidFileSize(long fileSize) {
        long maxSizeBytes = MAX_FILE_SIZE_MB * 1024 * 1024;
        return fileSize <= maxSizeBytes;
    }
}
