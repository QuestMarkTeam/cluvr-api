package com.example.chat.repository;

import java.util.List;

import com.example.chat.entity.ChatRoom;
import com.example.chat.enums.RoomType;
import com.example.common.repository.BaseRepository;

public interface ChatRoomRepository extends BaseRepository<ChatRoom, Long> {
	List<ChatRoom> findByClubId(Long clubId);

	List<ChatRoom> findByClubIdAndType(Long clubId, RoomType type);
}
