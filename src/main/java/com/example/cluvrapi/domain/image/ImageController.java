package com.example.cluvrapi.domain.image;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.cluvrapi.domain.image.service.ImageService;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;


	@GetMapping
	public Map<String, String> getImageName(@RequestParam String fileName) {
		return imageService.generatePresignedUploadUrl(fileName);
	}
}
