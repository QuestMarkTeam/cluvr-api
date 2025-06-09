package com.example.cluvrapi.domain.user.dto.request;

import com.example.cluvrapi.domain.category.enums.CategoryType;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

	@Email(message = "유효한 이메일 형식이 아닙니다.")
	private String email;

	private String phoneNumber;

	private CategoryType categoryType;

	private String imageUrl;
}
