package com.example.cluvrbatch.job.cloverlog;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.cluvrbatch.job.cloverlog.dto.CloverEventDto;
import com.example.cluvrbatch.job.cloverlog.repository.CloverLogJdbcRepository;

@Component
@RequiredArgsConstructor
public class CloverLogItemWriter implements ItemWriter<CloverEventDto> {

	private final CloverLogJdbcRepository cloverLogJdbcRepository;

	@Override
	public void write(Chunk<? extends CloverEventDto> chunk) {
		cloverLogJdbcRepository.batchInsert(chunk.getItems());
	}
}
