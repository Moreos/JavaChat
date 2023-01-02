package com.moreos.sockets.server;

import com.moreos.sockets.services.ChatRoomService;
import com.moreos.sockets.services.MessageService;
import com.moreos.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {

    private final MessageService messageService;
    private final UsersService usersService;
    private final ProduceConsumer produceConsumer;
    private final ChatRoomService chatRoomService;

    @Autowired
    public Server(MessageService messageService, UsersService usersService, ProduceConsumer produceConsumer, ChatRoomService chatRoomService) {
        this.messageService = messageService;
        this.usersService = usersService;
        this.produceConsumer = produceConsumer;
        this.chatRoomService = chatRoomService;
    }

    public void run(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            int count = 0;
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted " + (++count));

                new Thread(new ThreadServer(socket, messageService, usersService, produceConsumer, chatRoomService)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
