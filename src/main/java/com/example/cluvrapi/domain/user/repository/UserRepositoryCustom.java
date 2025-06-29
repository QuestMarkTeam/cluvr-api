package com.example.cluvrapi.domain.user.repository;

import java.util.List;
import java.util.Optional;

import com.example.cluvrapi.domain.user.entity.User;

/**
 * 설명: 사용자(User) 엔티티에 대한 커스텀 조회 메서드를 정의하는 인터페이스입니다.
 *
 * <p>삭제되지 않은 사용자 정보 조회, 이메일 중복 확인 등 기본 Spring Data JPA로 제공되지 않는
 *     복잡한 쿼리 로직을 구현합니다.
 *
 * @author 정승원
 */
public interface UserRepositoryCustom {

	/**
	 * 설명: 이메일을 기준으로 삭제되지 않은 사용자 엔티티를 조회합니다.
	 *
	 * <p>soft delete 처리된 사용자는 조회 대상에서 제외됩니다.
	 *
	 * @param email 설명: 조회할 사용자의 이메일
	 * @return Optional<User> 설명: 조회된 사용자(Optional.empty()일 수 있음)
	 */
	Optional<User> findByEmailAndNotDeleted(String email);

	/**
	 * 설명: 사용자 ID를 기준으로 삭제되지 않은 사용자의 보석(gem) 수량을 조회합니다.
	 *
	 * <p>soft delete 처리된 사용자는 제외되며, 보석 정보만 반환합니다.
	 *
	 * @param userId 설명: 조회할 사용자의 식별자
	 * @return Optional<Integer> 설명: 조회된 보석 수량(Optional.empty()일 수 있음)
	 */
	Optional<Integer> findGemByIdNotDeleted(Long userId);

	/**
	 * 설명: 사용자 ID를 기준으로 삭제되지 않은 사용자 엔티티를 조회합니다.
	 *
	 * <p>soft delete 처리된 사용자는 조회 대상에서 제외됩니다.
	 *
	 * @param userId 설명: 조회할 사용자의 식별자
	 * @return Optional<User> 설명: 조회된 사용자(Optional.empty()일 수 있음)
	 */
	Optional<User> findByIdNotDeleted(Long userId);

	/**
	 * 설명: 삭제되지 않은 모든 사용자 엔티티를 조회합니다.
	 *
	 * <p>soft delete 처리되지 않은(active) 사용자 목록을 반환합니다.
	 *
	 * @return List<User> 설명: 조회된 사용자 리스트
	 */
	List<User> findAllNotDeleted();

	/**
	 * 설명: 이메일 중복 여부를 확인합니다.
	 *
	 * <p>입력된 이메일을 가진 사용자가 이미 존재하는지 여부를 반환합니다.
	 *
	 * @param email 설명: 중복 검사를 위한 이메일
	 * @return boolean 설명: 이미 존재하면 true, 아니면 false
	 */
	boolean existsByEmail(String email);

	/**
	 * 설명: 전화번호 중복 여부를 확인합니다.
	 *
	 * <p>입력된 전화번호를 가진 사용자가 이미 존재하는지 여부를 반환합니다.
	 *
	 * @param phoneNumber 설명: 중복 검사를 위한 전화번호
	 * @return boolean 설명: 이미 존재하면 true, 아니면 false
	 */
	boolean existsByPhoneNumber(String phoneNumber);

}
