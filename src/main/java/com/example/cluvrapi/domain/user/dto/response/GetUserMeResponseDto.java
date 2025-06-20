package com.example.cluvrapi.domain.user.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.entity.enums.Gender;
import com.example.cluvrapi.domain.user.entity.enums.UserRole;

@Getter
@NoArgsConstructor
public class GetUserMeResponseDto {
	private Long id;
	private String name;
	private LocalDate birthday;
	private String email;
	private String phoneNumber;
	private UserRole userRole;
	private Gender gender;
	private CategoryType categoryType;
	private Integer gem;
	private String imageUrl;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public GetUserMeResponseDto(
		Long id,
		String name,
		LocalDate birthday,
		String email,
		String phoneNumber,
		UserRole userRole,
		Gender gender,
		CategoryType categoryType,
		Integer gem,
		String imageUrl,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		this.id = id;
		this.name = name;
		this.birthday = birthday;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.userRole = userRole;
		this.gender = gender;
		this.categoryType = categoryType;
		this.gem = gem;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static GetUserMeResponseDto from(User user) {
		return new GetUserMeResponseDto(
			user.getId(),
			user.getName(),
			user.getBirthday(),
			user.getEmail(),
			user.getPhoneNumber(),
			user.getUserRole(),
			user.getGender(),
			user.getCategoryType(),
			user.getGem(),
			user.getImageUrl(),
			user.getCreatedAt(),
			user.getModifiedAt()
		);
	}
}
