package com.example.cluvrapi.domain.analytics.entity;

import com.example.cluvrapi.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Getter
@Table(name = "point_statistics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer point;

	@Column(nullable = false)
	private Long userId;

	public PointStatistics(Integer point, Long userId) {
		this.point = point;
		this.userId = userId;
	}
}
