package com.moreos.sockets.services;

import com.moreos.sockets.models.ChatRoom;
import com.moreos.sockets.models.Message;
import com.moreos.sockets.models.User;
import com.moreos.sockets.repositories.ChatRoomRepository;
import com.moreos.sockets.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatRoomServiceImpl implements ChatRoomService{

    ChatRoomRepository chatRoomRepository;
    MessageRepository messageRepository;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }


    @Override
    public ChatRoom createRoom(String name, User user) {
        return chatRoomRepository.save(new ChatRoom(null, name, user.getId()));
    }

    @Override
    public ChatRoom enterRoom(Long room_id) {
        return chatRoomRepository.findById(room_id).get();
    }

    @Override
    public List<Message> takeLastMessages(ChatRoom chatRoom) {
        return messageRepository.findByRoom(chatRoom);
    }

    @Override
    public List<ChatRoom> takeRoomList() {
        return chatRoomRepository.findAll();
    }
}
