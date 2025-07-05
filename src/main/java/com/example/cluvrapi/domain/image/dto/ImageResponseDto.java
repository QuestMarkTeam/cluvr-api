package com.example.cluvrapi.domain.image.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.example.cluvrapi.domain.image.entity.Image;

@Getter
@RequiredArgsConstructor
public class ImageResponseDto {

    private final String imgUrl;

    private final String imgKey;

    public static ImageResponseDto toDto(Image image) {
        return new ImageResponseDto(
            image.getImgUrl(),
            image.getImgKey()
        );
    }

}
