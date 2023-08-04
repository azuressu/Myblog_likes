package com.example.post3.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j(topic = "S3 파일 변환")
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // MultipartFile을 전달받아서 File로 전환한 후에 S3에 업로드
    public String upload(MultipartFile multipartFile) throws IOException {
        // UUID와 기존의 file name으로 구성
        String filename = UUID.randomUUID() + multipartFile.getOriginalFilename();

        // file 업로드하기
        String fileUrl = fileUploadToS3(multipartFile, filename);

        // 로컬에 생긴 파일 삭제하기
        File convertFile = new File(multipartFile.getOriginalFilename());
        if(convertFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }

        return fileUrl;
    }

    // S3로 파일 업로드하기
    public String fileUploadToS3(MultipartFile multipartFile, String fileName) {
        String fileUrl = "http://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("이미지 크기가 적합하지 않습니다");
        }
    }

    // 로컬에서 파일 삭제하기
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // 파일 삭제하기
    public void deleteImage(String image) throws IOException {
        try {
            String fileName = image.substring(image.lastIndexOf("/") + 1);
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (SdkClientException e) {
            throw new IOException("S3에서 이미지 삭제 중 오류 발생: ", e);
        }
    }


}
