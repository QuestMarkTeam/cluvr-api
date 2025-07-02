package com.example.cluvrapi.domain.user.dto.request;

import java.time.LocalDate;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.user.entity.enums.Gender;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

	private String name;

	private String phoneNumber;

	private CategoryType categoryType;

	private String imageUrl;

	private Gender gender;

	private LocalDate birthday;
}
