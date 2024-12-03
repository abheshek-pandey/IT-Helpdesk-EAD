package com.ithelpdesk.util;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

public class S3FileStorage implements FileStorage {
    private final String bucketName;
    private final S3Client s3Client;

    public S3FileStorage(String bucketName, String region) {
        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
                .build();
    }

    @Override
    public String saveFile(String fileName, byte[] data) {
        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build(),
                RequestBody.fromBytes(data)
            );
            return fileName; // Return the S3 key (file name)
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to save file to S3: " + fileName, e);
        }
    }


    @Override
    public byte[] retrieveFile(String fileName) {
        try {
            ResponseInputStream<GetObjectResponse> objectStream = s3Client.getObject(
                GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build()
            );

            return objectStream.readAllBytes();
        } catch (IOException | S3Exception e) {
            throw new RuntimeException("Failed to retrieve file from S3: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(
                DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build()
            );
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to delete file from S3: " + fileName, e);
        }
    }
}
