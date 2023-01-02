package com.moreos.sockets.services;

import com.moreos.sockets.models.ChatRoom;
import com.moreos.sockets.models.Message;
import com.moreos.sockets.models.User;
import com.moreos.sockets.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageServiceImpl implements MessageService{
    MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public Message sendMessage(User author, String text, ChatRoom chatRoom) {
        Message message = new Message(null, author, chatRoom, text, null);
        messageRepository.save(message);
        return message;
    }
}
