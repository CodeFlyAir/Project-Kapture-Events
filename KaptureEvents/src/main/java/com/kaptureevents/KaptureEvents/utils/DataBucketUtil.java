package com.kaptureevents.KaptureEvents.utils;

import com.google.cloud.storage.*;
import com.kaptureevents.KaptureEvents.dto.FileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class DataBucketUtil {
    private final String bucketName;

    public DataBucketUtil(@Value("${gcp.bucket.name}") String bucketName) {
        this.bucketName = bucketName;
    }

    public FileDto uploadFile(MultipartFile file) {
        try {
            // Generate a unique file name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            log.info("Bucket name : {}", bucketName);
            log.info("File name : {}", fileName);
            // Create a BlobId associated with the file name
            BlobId blobId = BlobId.of(bucketName, fileName);
            log.info("Got BlobId");

            // Upload the file to Google Cloud Storage
            Storage storage = StorageOptions.getDefaultInstance().getService();
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();
            Blob blob = storage.create(blobInfo, file.getBytes());

            // Get the GCS URL of the uploaded file
            String fileUrl = blob.getMediaLink();

            // Create and return FileDto
            FileDto fileDto = new FileDto();
            fileDto.setFileName(fileName);
            fileDto.setFileUrl(fileUrl);

            return fileDto;
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            return null;
        }
    }

    public boolean deleteFile(String fileName) {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            Storage storage = StorageOptions.getDefaultInstance().getService();
            return storage.delete(blobId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
