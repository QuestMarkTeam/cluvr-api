package com.example.cluvrapi.domain.analytics.service;

import java.util.List;

public interface BusinessStatService {

	/**
	 * 설명: {레디스에 저장한 데이터 집계해줌 }
	 *
	 *
	 * @param redisKey {설명: 첫 번째 매개변수 설명}
	 * @return {반환값에 대한 설명}
	 *
	 * @author {작성자 이름}
	 */

	public <T> List<T> aggregate(String redisKey, Class<T> clazz);

	/**
	 *
	 * @param id 통계 식별자
	 *
	 * @author {작성자 이름}
	 */

	public void deleteStat(Long id);

	/**
	 *
	 * @param insertDtoList {설명: 벌크 인설트로 넣어줄 데이터 리스트 {설명: 첫 번째 매개변수 설명}}
	 *
	 * @author dnjs5024
	 */

	public <T> void insertStat(List<T> insertDtoList);
}
