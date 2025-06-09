package com.example.cluvrapi.domain.club.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.category.entity.Category;
import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.category.repository.CategoryRepository;
import com.example.cluvrapi.domain.club.dto.request.CreateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpdateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.response.CreateClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

	private final UserRepository userRepository;
	private final ClubRepository clubRepository;
	private final CategoryRepository categoryRepository;

	@Override
	@Transactional
	public CreateClubResponseDto createClub(Long userId, CreateClubRequestDto createClubRequestDto) {
		User findUser = userRepository.findByIdOrElseThrow(userId);

		Club newClub = new Club(
			createClubRequestDto.getName(),
			createClubRequestDto.getClubType(),
			createClubRequestDto.getMaxMemberCount(),
			createClubRequestDto.getMinScoreRequirement(),
			createClubRequestDto.getGreeting(),
			createClubRequestDto.getDescription(),
			createClubRequestDto.getPosterUrl(),
			createClubRequestDto.getIsPublic(),
			createClubRequestDto.getJoinType(),
			findUser
		);

		clubRepository.save(newClub);

		Category newCategory = new Category(
			newClub.getId(),
			createClubRequestDto.getCategoryDetail(),
			CategoryTargetType.CLUB
		);

		categoryRepository.save(newCategory);

		return CreateClubResponseDto.from(newClub.getId());
	}

	@Override
	public FindClubResponseDto findClubById(Long clubId) {
		return clubRepository.findClubById(clubId);
	}

	@Override
	public PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable) {
		return clubRepository.findAllClub(clubType, pageable);
	}

	@Override
	@Transactional
	public void updateClub(Long clubId, UpdateClubRequestDto updateClubRequestDto) {
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		if (updateClubRequestDto.getName() != null) {
			findClub.updateName(updateClubRequestDto.getName());
		}

		if (updateClubRequestDto.getGreeting() != null) {
			findClub.updateGreeting(updateClubRequestDto.getGreeting());
		}

		if (updateClubRequestDto.getDescription() != null) {
			findClub.updateDescription(updateClubRequestDto.getDescription());
		}
	}

	@Override
	public void deleteClub(Long clubId) {
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		clubRepository.delete(findClub);
	}
}
