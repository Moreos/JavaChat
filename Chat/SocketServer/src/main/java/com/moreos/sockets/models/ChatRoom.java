package com.moreos.sockets.models;

import java.util.Objects;

public class ChatRoom {
    private Long id;
    private String name;
    private Long owner_id;

    public ChatRoom(Long id, String name, Long owner_id) {
        this.id = id;
        this.name = name;
        this.owner_id = owner_id;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatroom = (ChatRoom) o;
        return id == chatroom.id &&
                Objects.equals(name, chatroom.name) &&
                Objects.equals(owner_id, chatroom.owner_id);
    }

    public String toString() {
        return "User{" + "id='" + id +
                "', name='" + name +
                "', owner='" + owner_id + "}";
    }

    public int hashCode() {
        return (int) (this.id * Math.random());
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwner() {
        return owner_id;
    }
}
