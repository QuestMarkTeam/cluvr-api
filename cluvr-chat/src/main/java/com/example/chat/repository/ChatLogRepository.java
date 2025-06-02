package com.example.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.chat.entity.ChatLog;

public interface ChatLogRepository extends MongoRepository<ChatLog, String> {
}
