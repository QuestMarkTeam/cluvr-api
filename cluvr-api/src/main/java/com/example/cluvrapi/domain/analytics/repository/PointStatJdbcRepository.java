package com.example.cluvrapi.domain.analytics.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.analytics.dto.CategoryStatRedisDto;
import com.example.cluvrapi.domain.analytics.dto.PointStatRedisDto;

@RequiredArgsConstructor
@Repository
public class PointStatJdbcRepository {
	private final JdbcTemplate jdbcTemplate;

	public void batchInsert(List<PointStatRedisDto> items) {
		String sql = "INSERT INTO PointStat (point,user_id) VALUES (?, ?)";
		// BatchPreparedStatementSetter 는 인덱스 기반
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PointStatRedisDto dto = items.get(i);
				ps.setInt(1, dto.getPoint());
				ps.setLong(2, dto.getUserId());
			}

			public int getBatchSize() {
				return items.size();
			}
		});
	}
}
