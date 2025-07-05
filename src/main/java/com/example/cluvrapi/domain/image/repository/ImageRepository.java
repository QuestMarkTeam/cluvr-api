package com.example.cluvrapi.domain.image.repository;

import com.example.cluvrapi.domain.image.entity.Image;
import com.example.cluvrapi.domain.image.entity.ImageType;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    default Image findByIdOrElseThrow(Long reviewImageId) {
        return findById(reviewImageId).orElseThrow(
            () -> new BusinessException(ResponseCode.NOT_FOUND)
        );
    }

    public List<Image> findByTargetIdAndType(Long targetId, ImageType type);

    default List<Image> findByTargetIdAndTypeElseThrow(Long targetId, ImageType type) {
        List<Image> list = findByTargetIdAndType(targetId, type);
        if (list.isEmpty()) throw new BusinessException(ResponseCode.NOT_FOUND);
        return list;
    }
}
