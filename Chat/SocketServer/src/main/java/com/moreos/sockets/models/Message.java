package com.moreos.sockets.models;

import java.sql.Date;

public class Message {

    private Long id;
    private User author;

    private ChatRoom chatRoom;
    private String text;
    private Date sendingTime;

    public Message(Long id, User author, ChatRoom chatRoom, String text, Date sendingTime) {
        this.id = id;
        this.author = author;
        this.chatRoom = chatRoom;
        this.text = text;
        this.sendingTime = sendingTime;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Date getSendingTime() {
        return sendingTime;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
