package com.example.cluvrapi.domain.til.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.til.dto.reqeust.CreateTilRequestDto;
import com.example.cluvrapi.domain.til.dto.reqeust.UpdateTilRequestDto;
import com.example.cluvrapi.domain.til.dto.response.CreateTilResponseDto;
import com.example.cluvrapi.domain.til.dto.response.InfoTilResponseDto;
import com.example.cluvrapi.domain.til.service.TilService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/clubs/{clubId}/til")
@RequiredArgsConstructor
public class TilController {

	private final TilService tilService;

	@PostMapping
	public ResponseEntity<BaseResponse<CreateTilResponseDto>> createTil(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@Valid @RequestBody CreateTilRequestDto createTilRequestDto
	) {
		CreateTilResponseDto createTilResponseDto = tilService.createTil(authUser.id(), clubId, createTilRequestDto);
		return ResponseEntity.ok(BaseResponse.success(createTilResponseDto, ResponseCode.CREATED));
	}

	@GetMapping("/{tilId}")
	public ResponseEntity<BaseResponse<InfoTilResponseDto>> findTilById(
		@PathVariable Long tilId
	) {
		InfoTilResponseDto infoTilResponseDto = tilService.findTilById(tilId);
		return ResponseEntity.ok(BaseResponse.success(infoTilResponseDto, ResponseCode.OK));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<InfoTilResponseDto>>> findAllTilById(
		@PathVariable Long clubId,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<InfoTilResponseDto> pageResponseDto = tilService.findAllTilById(clubId, pageable);
		return ResponseEntity.ok(BaseResponse.success(pageResponseDto, ResponseCode.OK));
	}

	@PatchMapping("/{tilId}")
	public ResponseEntity<BaseResponse<Void>> updateTil(
		@Auth AuthUser authUser,
		@PathVariable Long tilId,
		@Valid @RequestBody UpdateTilRequestDto updateTilRequestDto
	) {
		tilService.updateTil(authUser.id(), tilId, updateTilRequestDto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@DeleteMapping("/{tilId}")
	public ResponseEntity<BaseResponse<Void>> deleteTil(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long tilId
	) {
		tilService.deleteTil(authUser.id(), clubId, tilId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
