package com.example.cluvrapi.domain.image.service;

import com.example.cluvrapi.domain.image.dto.ImageResponseDto;
import com.example.cluvrapi.domain.image.entity.ImageType;

import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void fileSave(String url, String key, Long targetId, ImageType type);

    List<ImageResponseDto> find(Long targetId, ImageType type);

    void update(Long targetId, ImageType imageType, String url, String key);

    void delete(List<String> keys,ImageType imageType, Long targetId);

    Map<String, String> generatePresignedUploadUrl(String originalFileName);

    public String moveImageToUserProfile(String tempFileUrl,Long targetId, ImageType type);

}
