package com.moreos.sockets.services;

import com.moreos.sockets.models.ChatRoom;
import com.moreos.sockets.models.Message;
import com.moreos.sockets.models.User;

import java.util.List;

public interface ChatRoomService {
    ChatRoom createRoom(String name, User user);
    ChatRoom enterRoom(Long room_id);
    List<Message> takeLastMessages(ChatRoom chatRoom);
    List<ChatRoom> takeRoomList();
}
