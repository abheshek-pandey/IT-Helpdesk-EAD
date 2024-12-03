package com.ithelpdesk.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AttachmentService {
    private String localPath;
    private String s3Bucket;
    private String s3Region;

    // Local storage constructor
    public AttachmentService(String localPath) {
        this.localPath = localPath;
        System.out.println("AttachmentService initialized for local storage: " + localPath);
    }

    // S3 storage constructor
    public AttachmentService(String s3Bucket, String s3Region) {
        this.s3Bucket = s3Bucket;
        this.s3Region = s3Region;
        System.out.println("AttachmentService initialized for S3 storage: " + s3Bucket + " in region " + s3Region);
    }

    // Save file locally
    public void saveLocalAttachment(String filename, byte[] fileData) throws IOException {
        Path filePath = Paths.get(localPath, filename);
        Files.createDirectories(filePath.getParent()); // Ensure directories exist
        Files.write(filePath, fileData);
        System.out.println("File saved locally: " + filePath);
    }

    // Placeholder for S3 storage
    public void saveToS3(String filename, byte[] fileData) {
        System.out.println("Saving file to S3 (Placeholder): " + filename);
        // Add S3 upload logic here using AWS SDK for Java (e.g., AmazonS3.putObject())
    }

    // Determine storage type and save
    public void saveAttachment(String filename, byte[] fileData) throws IOException {
        if (localPath != null) {
            saveLocalAttachment(filename, fileData);
        } else if (s3Bucket != null && s3Region != null) {
            saveToS3(filename, fileData);
        } else {
            throw new IllegalStateException("No storage method configured!");
        }
    }
}
