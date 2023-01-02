package com.moreos.client.connect;

import java.io.*;
import java.net.Socket;

public class SendMessage {
    public static void request(String login, String text, Socket socket) throws IOException {

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        System.out.println(login + ":" + text.replace('|', ' '));
        out.write("message|" + login + "|" + text.replace('|', ' '));
        out.newLine();
        out.flush();
    }
}
