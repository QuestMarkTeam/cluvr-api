package com.example.chat.repository;

import java.util.List;
import java.util.Optional;

import com.example.chat.entity.ChatRoomUser;
import com.example.common.repository.BaseRepository;

public interface ChatRoomUserRepository extends BaseRepository<ChatRoomUser, Long> {
	boolean existsByRoomIdAndUserId(Long roomId, Long userId);

	List<ChatRoomUser> findByRoomId(Long roomId);

	Optional<ChatRoomUser> findByRoomIdAndUserId(Long roomId, Long userId);

	void deleteByRoomIdAndUserId(Long roomId, Long userId);

	List<ChatRoomUser> findByUserId(Long userId);
}
