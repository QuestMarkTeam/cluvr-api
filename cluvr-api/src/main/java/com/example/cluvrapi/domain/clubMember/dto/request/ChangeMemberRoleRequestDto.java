package com.example.cluvrapi.domain.clubMember.dto.request;

import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeMemberRoleRequestDto {
	@NotNull(message = "변경할 역할을 지정하세요.")
	private ClubMemberRole newRole;

	public void setNewRole(ClubMemberRole newRole) {
		this.newRole = newRole;
	}
}
