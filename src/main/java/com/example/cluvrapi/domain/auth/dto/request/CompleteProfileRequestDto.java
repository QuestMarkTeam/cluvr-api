package com.example.cluvrapi.domain.auth.dto.request;

import java.time.LocalDate;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.user.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompleteProfileRequestDto {
	@NotBlank
	private final String phoneNumber;
	@NotNull
	private final LocalDate birthday;
	@NotNull  private final Gender gender;
	@NotNull  private final CategoryType categoryType;
	@NotNull private final String name;

	@JsonCreator
	public CompleteProfileRequestDto(
		@JsonProperty("phoneNumber") String phoneNumber,
		@JsonProperty("birthday")    LocalDate birthday,
		@JsonProperty("gender")      Gender gender,
		@JsonProperty("categoryType") CategoryType categoryType,
		@JsonProperty("name") String name
	) {
		this.phoneNumber = phoneNumber;
		this.birthday    = birthday;
		this.gender      = gender;
		this.categoryType = categoryType;
		this.name = name;
	}

	public String getPhoneNumber() { return phoneNumber; }
	public LocalDate getBirthday() { return birthday; }
	public Gender getGender()      { return gender; }
	public CategoryType getCategoryType() { return categoryType; }
	public String getName(){return name;}
}
