package com.moreos.client.connect;

import com.moreos.client.forms.AuthenticateForm;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class Request {
    public static boolean request(String type, AuthenticateForm form, Socket socket) throws IOException {

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Map<String, String> map = form.enterForm();

        out.write(type + "|" + map.get("Login") + "|" + map.get("Password"));
        out.newLine();
        out.flush();

        return in.readLine().equals("status|successful");
    }
}
