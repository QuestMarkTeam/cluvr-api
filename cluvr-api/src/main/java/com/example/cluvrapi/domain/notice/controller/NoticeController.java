package com.example.cluvrapi.domain.notice.controller;

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

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notice.dto.reqeust.CreateNoticeRequestDto;
import com.example.cluvrapi.domain.notice.dto.reqeust.UpdateNoticeRequestDto;
import com.example.cluvrapi.domain.notice.dto.response.CreateNoticeResponseDto;
import com.example.cluvrapi.domain.notice.dto.response.InfoNoticeResponseDto;
import com.example.cluvrapi.domain.notice.service.NoticeService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/clubs/{clubId}/notices")
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;

	@PostMapping
	public ResponseEntity<BaseResponse<CreateNoticeResponseDto>> createNotice(
		@PathVariable Long clubId,
		@RequestBody CreateNoticeRequestDto createNoticeRequestDto
	) {
		CreateNoticeResponseDto createNoticeResponseDto = noticeService.createNotice(1L, clubId,
			createNoticeRequestDto);
		return ResponseEntity.ok(BaseResponse.success(createNoticeResponseDto, ResponseCode.CREATED));
	}

	@GetMapping("/{noticeId}")
	public ResponseEntity<BaseResponse<InfoNoticeResponseDto>> findNoticeById(
		@PathVariable Long clubId,
		@PathVariable Long noticeId
	) {
		InfoNoticeResponseDto infoNoticeResponseDto = noticeService.findNoticeById(clubId, noticeId);
		return ResponseEntity.ok(BaseResponse.success(infoNoticeResponseDto, ResponseCode.OK));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<InfoNoticeResponseDto>>> findAllNotice(
		@PathVariable Long clubId,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<InfoNoticeResponseDto> pageResponseDto = noticeService.findAllNotice(clubId, pageable);
		return ResponseEntity.ok(BaseResponse.success(pageResponseDto, ResponseCode.OK));
	}

	@PatchMapping("/{noticeId}")
	public ResponseEntity<BaseResponse<Void>> updateNotice(
		@PathVariable Long noticeId,
		@RequestBody UpdateNoticeRequestDto updateNoticeRequestDto
	) {
		noticeService.updateNotice(noticeId, updateNoticeRequestDto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	@DeleteMapping("/{noticeId}")
	public ResponseEntity<BaseResponse<Void>> deleteNotice(
		@PathVariable Long noticeId
	) {
		noticeService.deleteNotice(noticeId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

}
