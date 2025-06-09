package com.example.cluvrapi.domain.analytics.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.analytics.dto.UserBoardStatRedisDto;

@RequiredArgsConstructor
@Repository
public class UserBoardStatJdbcRepository {
	private final JdbcTemplate jdbcTemplate;

	public void batchInsert(List<UserBoardStatRedisDto> items) {
		String sql = "INSERT INTO UserBoard (score,user_id,category_id) VALUES (?, ?, ?)";
		// BatchPreparedStatementSetter 는 인덱스 기반
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserBoardStatRedisDto dto = items.get(i);
				ps.setInt(1, dto.getScore());
				ps.setLong(2, dto.getUserId());
				ps.setLong(3, dto.getCategoryId());

			}

			public int getBatchSize() {
				return items.size();
			}
		});
	}
}
