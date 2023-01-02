package com.moreos.sockets.repositories;

import com.moreos.sockets.models.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    List<ChatRoom> findAll();
    Optional<ChatRoom> findById(Long id);
    ChatRoom save(ChatRoom chatRoom);
}
