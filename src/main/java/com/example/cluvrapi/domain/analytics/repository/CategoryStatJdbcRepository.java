package com.example.cluvrapi.domain.analytics.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.analytics.dto.CategoryStatRedisDto;

@RequiredArgsConstructor
@Repository
public class CategoryStatJdbcRepository {
	private final JdbcTemplate jdbcTemplate;

	public void batchInsert(List<CategoryStatRedisDto> items) {
		String sql = "INSERT INTO CategoryStat (total_answer, total_selected, total_score, total_question,user_id,category_id) VALUES (?, ?, ?, ?,? ,?)";
		// BatchPreparedStatementSetter 는 인덱스 기반
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			public void setValues(PreparedStatement ps, int i) throws SQLException {
				CategoryStatRedisDto dto = items.get(i);
				ps.setInt(1, dto.getTotalAnswer());
				ps.setInt(2, dto.getTotalSelected());
				ps.setInt(3, dto.getTotalScore());
				ps.setInt(4, dto.getTotalQuestion());
				ps.setLong(5, dto.getUserId());
				ps.setLong(6, dto.getCategoryId());
			}

			public int getBatchSize() {
				return items.size();
			}
		});
	}
}
