package com.example.cluvrapi.domain.clubMember.dto.request;

import com.example.cluvrapi.domain.join.enums.JoinStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HandleJoinStatusRequestDto {
	private JoinStatus status;
	
	public void setStatus(JoinStatus status) {
		this.status = status;
	}
}
