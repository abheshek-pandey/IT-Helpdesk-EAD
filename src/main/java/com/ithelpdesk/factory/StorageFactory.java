package com.ithelpdesk.factory;

import com.ithelpdesk.util.FileStorage;
import com.ithelpdesk.util.LocalFileStorage;
import com.ithelpdesk.util.S3FileStorage;
import com.ithelpdesk.util.ConfigLoader;

public class StorageFactory {
    public static FileStorage getFileStorage() {
        // Retrieve the storage type from configuration
        String storageType = ConfigLoader.getConfig("attachments.storage").toLowerCase(); 

        // Ensure valid storage type
        if (storageType == null || storageType.isEmpty()) {
            throw new IllegalArgumentException("Storage type not specified in configuration.");
        }

        switch (storageType) {
            case "local":
                // Ensure the local path is configured
                String localPath = ConfigLoader.getConfig("attachments.local.path");
                if (localPath == null || localPath.isEmpty()) {
                    throw new IllegalArgumentException("Local storage path is not configured.");
                }
                return new LocalFileStorage(localPath);
                
            case "s3":
                // Ensure the necessary S3 configurations are present
                String bucketName = ConfigLoader.getConfig("s3.bucket_name");
                String region = ConfigLoader.getConfig("s3.region");

                if (bucketName == null || bucketName.isEmpty()) {
                    throw new IllegalArgumentException("S3 bucket name is not configured.");
                }
                if (region == null || region.isEmpty()) {
                    throw new IllegalArgumentException("S3 region is not configured.");
                }
                
                return new S3FileStorage(bucketName, region);
                
            default:
                throw new IllegalArgumentException("Unsupported storage type: " + storageType);
        }
    }
}
