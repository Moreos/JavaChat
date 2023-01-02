package com.moreos.client.connect;

import com.moreos.client.forms.AuthenticateForm;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ConnectToChat {
    public static String request(String type, AuthenticateForm form, Socket socket) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Map<String, String> map = form.enterForm();

        JSONObject outJson = new JSONObject();

        outJson.put("type", type);
        outJson.put("login", map.get("login"));
        outJson.put("password", map.get("password"));
        System.out.println("out json " + outJson.toString());
        out.write(outJson.toString());
        out.newLine();
        out.flush();

        return in.readLine();
    }
}
