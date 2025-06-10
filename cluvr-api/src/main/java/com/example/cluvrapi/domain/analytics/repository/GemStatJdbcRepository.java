package com.example.cluvrapi.domain.analytics.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.analytics.dto.GemStatRedisDto;

@RequiredArgsConstructor
@Repository
public class GemStatJdbcRepository {
	private final JdbcTemplate jdbcTemplate;

	public void batchInsert(List<GemStatRedisDto> items) {
		String sql = "INSERT INTO GemStat (gem,user_id) VALUES (?, ?)";
		// BatchPreparedStatementSetter 는 인덱스 기반
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			public void setValues(PreparedStatement ps, int i) throws SQLException {
				GemStatRedisDto dto = items.get(i);
				ps.setInt(1, dto.getGem());
				ps.setLong(2, dto.getUserId());
			}

			public int getBatchSize() {
				return items.size();
			}
		});
	}
}
