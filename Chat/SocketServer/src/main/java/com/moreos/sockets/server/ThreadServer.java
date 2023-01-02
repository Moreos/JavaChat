package com.moreos.sockets.server;

import com.moreos.sockets.models.ChatRoom;
import com.moreos.sockets.models.Message;
import com.moreos.sockets.models.User;
import com.moreos.sockets.services.ChatRoomService;
import com.moreos.sockets.services.MessageService;
import com.moreos.sockets.services.UsersService;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ThreadServer implements Runnable {
    private final Socket socket;
    private User user;
    private ChatRoom chatRoom;
    private final MessageService messageService;
    private final UsersService usersService;
    private final ProduceConsumer produceConsumer;
    private final ChatRoomService chatRoomService;

    private Message lastSendMessage;

    public ThreadServer(Socket socket, MessageService messageService, UsersService usersService, ProduceConsumer produceConsumer, ChatRoomService chatRoomService) {
        this.socket = socket;
        this.messageService = messageService;
        this.usersService = usersService;
        this.produceConsumer = produceConsumer;
        this.chatRoomService = chatRoomService;
    }

    @Override
    public void run() {
        try {
            BufferedWriter out;
            BufferedReader in;
            boolean closeConnect = false;
            do {
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                JSONObject outJson = new JSONObject();

                if (in.ready()) {
                    String input = in.readLine();
                    System.out.println(input);
                    JSONObject jsonObject = new JSONObject(input);
                    switch (jsonObject.getString("type")) {
                        case "signUp":
                            if (!usersService.register(jsonObject.getString("login"), jsonObject.getString("password"))) {
                                System.out.println("User registration " + jsonObject.getString("login"));
                            } else {
                                closeConnect = true;
                            }
                        case "signIn":
                            if (!closeConnect && (user = usersService.authenticate(jsonObject.getString("login"), jsonObject.getString("password"))) != null) {
                                System.out.println("Enter user " + jsonObject.getString("login"));
                                outJson.put("status", "successfully");
                                List<Long> rooms_id = new ArrayList<>();
                                List<String> rooms_name = new ArrayList<>();
                                for (ChatRoom r : chatRoomService.takeRoomList()) {
                                    rooms_id.add(r.getId());
                                    rooms_name.add(r.getName());
                                }
                                outJson.put("chatRooms_ids", rooms_id);
                                outJson.put("chatRooms_names", rooms_name);
                            } else {
                                outJson.put("status", "failed");
                                closeConnect = true;
                            }

                            break;
                        case "createRoom":
                            chatRoomService.createRoom(jsonObject.getString("roomName"), user);
                            System.out.println("Create room: " + jsonObject.getString("roomName"));
                            outJson.put("status", "successfully");
                            List<Long> rooms_id = new ArrayList<>();
                            List<String> rooms_name = new ArrayList<>();
                            for (ChatRoom r : chatRoomService.takeRoomList()) {
                                rooms_id.add(r.getId());
                                rooms_name.add(r.getName());
                            }
                            outJson.put("chatRooms_ids", rooms_id);
                            outJson.put("chatRooms_names", rooms_name);
                            break;
                        case "enterRoom":
                            chatRoom = chatRoomService.enterRoom(jsonObject.getLong("roomId"));
                            outJson.put("status", "successfully");
                            List<String> sender = new ArrayList<>();
                            List<String> message = new ArrayList<>();
                            for (Message m : chatRoomService.takeLastMessages(chatRoom)) {
                                sender.add(m.getAuthor().getLogin());
                                message.add(m.getText());
                            }
                            outJson.put("messageSender", sender);
                            outJson.put("message", message);
                            break;
                        case "sendMessage":
                            try {
                                produceConsumer.produce(jsonObject.getLong("roomId"), messageService.sendMessage(user, jsonObject.getString("message"), chatRoom));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "exit":
                            if (chatRoom != null) {
                                outJson.put("exitRoom", "complete");
                                chatRoom = null;
                            } else {
                                usersService.sessionClose(user);
                                closeConnect = true;
                            }
                            break;
                    }
                    out.write(outJson.toString());
                    out.newLine();
                    out.flush();
                }
                try {
                    if (chatRoom != null) {
                        Message message = produceConsumer.consume(chatRoom.getId());
                        if (message != null && user != null && !message.getAuthor().getLogin().equals(user.getLogin())) {
                            if (!message.equals(lastSendMessage)) {
                                lastSendMessage = message;
                                outJson.put("type", "message");
                                outJson.put("sender", message.getAuthor().getLogin());
                                outJson.put("message", message.getText());
                                out.write(outJson.toString());
                                out.newLine();
                                out.flush();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } while (!closeConnect);
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
