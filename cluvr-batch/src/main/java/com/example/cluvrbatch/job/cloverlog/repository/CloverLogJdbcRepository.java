package com.example.cluvrbatch.job.cloverlog.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cluvrbatch.job.cloverlog.dto.CloverEventDto;

@RequiredArgsConstructor
@Repository
public class CloverLogJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void batchInsert(List<? extends CloverEventDto> items) {
		String sql = "INSERT INTO clover_log " +
			"(user_id, amount, created_at, deleted_at, description, action, flow_type) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				CloverEventDto dto = items.get(i);
				ps.setLong(1, dto.getUserId());
				ps.setInt(2, dto.getAmount());
				ps.setTimestamp(3, Timestamp.valueOf(dto.getCreatedAt()));
				ps.setTimestamp(4, dto.getDeletedAt() != null ? Timestamp.valueOf(dto.getDeletedAt()) : null);
				ps.setString(5, dto.getDescription());
				ps.setString(6, dto.getAction());
				ps.setString(7, dto.getFlowType().name()); // enum은 보통 String으로 저장
			}

			@Override
			public int getBatchSize() {
				return items.size();
			}
		});
	}
}
