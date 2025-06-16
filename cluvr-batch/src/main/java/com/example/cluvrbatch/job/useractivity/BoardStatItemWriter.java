package com.example.cluvrbatch.job.useractivity;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.cluvrbatch.job.useractivity.dto.BoardStatEventDto;
import com.example.cluvrbatch.job.useractivity.repository.BoardStatJdbcRepository;

@Component
@RequiredArgsConstructor
public class BoardStatItemWriter implements ItemWriter<BoardStatEventDto> {

	private final BoardStatJdbcRepository boardStatJdbcRepository;

	@Override
	public void write(Chunk<? extends BoardStatEventDto> chunk) throws Exception {
		boardStatJdbcRepository.batchInsert(chunk.getItems());
	}
}
