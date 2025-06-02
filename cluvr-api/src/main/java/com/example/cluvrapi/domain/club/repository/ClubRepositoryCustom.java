package com.example.cluvrapi.domain.club.repository;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;

public interface ClubRepositoryCustom {
	FindClubResponseDto findClubById(Long clubId);

	PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable);
}
