//package com.newtech.Services;
//
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class CloudinaryService {
//
//    private final Cloudinary cloudinary;
//
//    public CloudinaryService(
//            @Value("${cloudinary.cloud_name}") String cloudName,
//            @Value("${cloudinary.api_key}") String apiKey,
//            @Value("${cloudinary.api_secret}") String apiSecret) {
//
//        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "dfj5l1xmm", cloudName,
//                "567433548161939", apiKey,
//                "pC3PYUYhdR95IadKGVqb1oSnrj0", apiSecret));
//    }
//
//    public String uploadImage(MultipartFile file) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        return (String) uploadResult.get("url");
//    }
//}
