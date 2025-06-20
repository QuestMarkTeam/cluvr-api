package com.example.cluvrapi.domain.analytics.service;

import java.util.List;

public interface BusinessStatService {

	/**
	 * 설명: {레디스에 저장한 데이터 집계해줌 }
	 *
	 *
	 * @param redisKey {설명: 첫 번째 매개변수 설명}
	 * @return {redis 에서 가져온 데이터를 insertStat 로 리턴해줌}
	 *
	 * @author dnjs5024
	 */

	public <T> List<T> aggregate(String redisKey, Class<T> clazz);

	/**
	 *
	 * @param id 통계 식별자
	 *
	 * @author dnjs5024
	 */

	public void deleteStat(Long id);

	/**
	 * 설명: aggregate메소드에서 반환해준 리스트 매개변수로 받음
	 *
	 * @param insertDtoList {설명: 벌크 인설트로 넣어줄 데이터 리스트 {설명: 첫 번째 매개변수 설명}}
	 *
	 * @author dnjs5024
	 */

	public <T> void insertStat(List<T> insertDtoList);
}
