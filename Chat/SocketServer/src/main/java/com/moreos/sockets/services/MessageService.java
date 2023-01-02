package com.moreos.sockets.services;

import com.moreos.sockets.models.ChatRoom;
import com.moreos.sockets.models.Message;
import com.moreos.sockets.models.User;

public interface MessageService {
    Message sendMessage(User author, String text, ChatRoom chatRoom);
}
