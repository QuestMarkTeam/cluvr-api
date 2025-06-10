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
public class GetUserOtherResponseDto {
	private Long id;
	private String name;
	private LocalDate birthday;
	private Gender gender;
	private CategoryType categoryType;
	private UserRole userRole;
	private Integer gem;
	private String imageUrl;
	private LocalDateTime createdAt;

	public GetUserOtherResponseDto(
		Long id,
		String name,
		LocalDate birthday,
		Gender gender,
		CategoryType categoryType,
		UserRole userRole,
		Integer gem,
		String imageUrl,
		LocalDateTime createdAt
	) {
		this.id = id;
		this.name = name;
		this.birthday = birthday;
		this.gender = gender;
		this.categoryType = categoryType;
		this.userRole = userRole;
		this.gem = gem;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}

	public static GetUserOtherResponseDto from(User user) {
		return new GetUserOtherResponseDto(
			user.getId(),
			user.getName(),
			user.getBirthday(),
			user.getGender(),
			user.getCategoryType(),
			user.getUserRole(),
			user.getGem(),
			user.getImageUrl(),
			user.getCreatedAt()
		);
	}
}

