package com.example.cluvrapi.domain.category.repository;

import java.util.Optional;

import com.example.cluvrapi.domain.category.entity.Category;
import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.common.repository.BaseRepository;

public interface CategoryRepository extends BaseRepository<Category, Long> {
	Optional<Category> findByTargetIdAndTargetType(Long targetId, CategoryTargetType targetType);

}
