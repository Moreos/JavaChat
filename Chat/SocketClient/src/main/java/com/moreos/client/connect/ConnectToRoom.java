package com.moreos.client.connect;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ConnectToRoom {
    public static String request(String type, Long roomId, Socket socket) throws IOException {

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        JSONObject outJson = new JSONObject();

        outJson.put("type", type);
        outJson.put("roomId", roomId);
        System.out.println("out json " + outJson.toString());
        out.write(outJson.toString());
        out.newLine();
        out.flush();

        return in.readLine();
    }
}
