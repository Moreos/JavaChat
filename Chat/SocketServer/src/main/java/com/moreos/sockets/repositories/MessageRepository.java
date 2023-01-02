package com.moreos.sockets.repositories;

import com.moreos.sockets.models.ChatRoom;
import com.moreos.sockets.models.Message;

import java.util.List;

public interface MessageRepository {
    public List<Message> findByRoom(ChatRoom chatRoom);
    public void save(Message message);
}
