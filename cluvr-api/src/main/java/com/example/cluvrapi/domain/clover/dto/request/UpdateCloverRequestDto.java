package com.example.cluvrapi.domain.clover.dto.request;

import lombok.Getter;

import com.example.cluvrapi.domain.user.entity.User;

@Getter
public class UpdateCloverRequestDto {
	private Integer score;
	private User user;
}
