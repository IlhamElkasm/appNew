//package com.newtech.Services;
//
//
//import io.minio.*;
//import io.minio.http.Method;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class FileStorageService {
//    @Autowired
//    private MinioClient minioClient;
//
//    @Value("${minio.bucket}")
//    private String bucketName;
//
//    public String uploadFile(MultipartFile file) throws Exception {
//        // Create bucket if it doesn't exist
//        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//        if (!found) {
//            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//        }
//
//        // Generate unique filename
//        String originalFilename = file.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String filename = UUID.randomUUID() + extension;
//
//        // Upload file
//        minioClient.putObject(
//                PutObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(filename)
//                        .contentType(file.getContentType())
//                        .stream(file.getInputStream(), file.getSize(), -1)
//                        .build()
//        );
//
//        // Generate URL for the file
//        String url = minioClient.getPresignedObjectUrl(
//                GetPresignedObjectUrlArgs.builder()
//                        .bucket(bucketName)
//                        .object(filename)
//                        .method(Method.GET)
//                        .expiry(7, TimeUnit.DAYS)
//                        .build()
//        );
//
//        // Return the URL or just the filename
//        return filename;
//    }
//
//    public String getFileUrl(String filename) throws Exception {
//        return minioClient.getPresignedObjectUrl(
//                GetPresignedObjectUrlArgs.builder()
//                        .bucket(bucketName)
//                        .object(filename)
//                        .method(Method.GET)
//                        .expiry(7, TimeUnit.DAYS)
//                        .build()
//        );
//    }
//
//    public void deleteFile(String filename) throws Exception {
//        minioClient.removeObject(
//                RemoveObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(filename)
//                        .build()
//        );
//    }
//}
//
