package com.example.cluvrapi.domain.image.service;

import com.example.cluvrapi.domain.image.dto.ImageResponseDto;
import com.example.cluvrapi.domain.image.entity.Image;
import com.example.cluvrapi.domain.image.entity.ImageType;
import com.example.cluvrapi.domain.image.repository.ImageRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

	private final S3Presigner s3Presigner;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	private final S3Client s3Client;

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	/**
	 * aws 에 사진 업로드 하고 이미지 파일 db에 저장
	 *
	 * @param targetId 타겟 식별자 ex) 가게 , 리뷰 등
	 * @param type     타겟 타입 ex) 가게 , 리뷰 등
	 */
	@Override
	@Transactional
	public void fileSave(String url, String key, Long targetId, ImageType type) {
		try {// db에 저장 실패 or aws 에 업로드 실패 시 에러
			imageRepository.save(Image.of(targetId, url, type, key));
		} catch (Exception e) {
			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder() // aws 에 이미 올라간 이미지 삭제
				.bucket(bucket)  // 연결 된 대상 버킷 이름
				.key(key)  // 버킷 내 삭제할 객체 키
				.build();
			s3Client.deleteObject(deleteObjectRequest);
			throw new BusinessException(ResponseCode.NOT_FOUND); // 롤백위해 오류 날림
		}

	}

	/**
	 * 타겟 식별자,타입으로 조회한 후 결과 url 를 String List 에 담아서 반환
	 *
	 * @param targetId 타겟 식별자 ex) 가게 , 리뷰 등
	 * @param type     타겟 타입 ex) 가게 , 리뷰 등
	 * @return List<String> 이미지 url
	 */
	@Override
	public List<ImageResponseDto> find(Long targetId, ImageType type) {
		return imageRepository.findByTargetIdAndTypeElseThrow(targetId, type).stream()
			.map(ImageResponseDto::toDto).toList();
	}

	@Override
	public void update(Long targetId, ImageType imageType, String url, String key) {
		delete(find(targetId, imageType).stream().map(ImageResponseDto::getImgKey).toList(),
			imageType, targetId);// 기존 사진 삭제
		fileSave(url ,key , targetId, imageType);
	}

	@Override
	public void delete(List<String> keys, ImageType imageType, Long targetId) {
		for (String key : keys) {
			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucket)  // 연결 된 대상 버킷 이름
				.key(key)  // 버킷 내 삭제할 객체 키
				.build();
			s3Client.deleteObject(deleteObjectRequest);
		}

		imageRepository.deleteAll(
			imageRepository.findByTargetIdAndTypeElseThrow(targetId, imageType));
	}

	public Map<String, String> generatePresignedUploadUrl(String originalFileName) {
		String key = "temp/" + UUID.randomUUID() + "_" + originalFileName;

		String contentType = getContentTypeByExtension(originalFileName);

		AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
		S3Presigner presigner = S3Presigner.builder()
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.build();

		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.contentType(contentType)
			.build();

		PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(builder -> builder
			.signatureDuration(Duration.ofMinutes(5))
			.putObjectRequest(putObjectRequest));

		URL uploadUrl = presignedRequest.url();

		Map<String, String> result = new HashMap<>();
		result.put("uploadUrl", uploadUrl.toString());
		result.put("fileUrl", "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key);
		return result;
	}

	private String getContentTypeByExtension(String fileName) {
		String lowerCase = fileName.toLowerCase();
		if (lowerCase.endsWith(".png")) {
			return "image/png";
		} else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) {
			return "image/jpeg";
		} else if (lowerCase.endsWith(".webp")) {
			return "image/webp";
		}
		// 기본 fallback
		return "application/octet-stream";
	}

	@Override
	public String moveImageToUserProfile(String tempFileUrl,Long targetId, ImageType type) {
		String tempKey = extractKeyFromUrl(tempFileUrl); // ex) temp/abcd_filename.png
		String newKey = "images/" + UUID.randomUUID() + "_" + extractFilename(tempKey);

		try {
			// 1. 이미지 복사
			CopyObjectRequest copyRequest = CopyObjectRequest.builder()
				.sourceBucket(bucket)
				.sourceKey(tempKey)
				.destinationBucket(bucket)
				.destinationKey(newKey)
				.build();
			s3Client.copyObject(copyRequest);

			// 3. DB 저장 (실패 시 복사본 삭제)
			String url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + newKey;
			System.out.println(url);
			fileSave(url, newKey, targetId, type);

			// 2. 원본(temp) 이미지 삭제
			s3Client.deleteObject(DeleteObjectRequest.builder()
				.bucket(bucket)
				.key(tempKey)
				.build());

			return url;

		} catch (Exception e) {
			// 복사 후 실패 시 복사된 객체 정리
			try {
				s3Client.deleteObject(DeleteObjectRequest.builder()
					.bucket(bucket)
					.key(newKey)
					.build());
			} catch (Exception cleanupEx) {
				// 로그만 남김 (실패해도 다음 예외가 우선)
				System.err.println("복사된 이미지 삭제 실패: " + cleanupEx.getMessage());
			}
			throw new BusinessException(ResponseCode.INTERNAL_ERROR, "이미지 이동 중 오류 발생");
		}
	}
	private String extractFilename(String key) {
		return key.substring(key.lastIndexOf('/') + 1);
	}

	private String extractKeyFromUrl(String fileUrl) {
		String prefix = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
		return fileUrl.replace(prefix, "");
	}
}
