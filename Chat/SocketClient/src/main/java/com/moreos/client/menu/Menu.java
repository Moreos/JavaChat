package com.moreos.client.menu;

import com.moreos.client.connect.*;
import com.moreos.client.forms.AuthenticateForm;
import com.moreos.client.forms.SignInAuthenticateForm;
import com.moreos.client.forms.SignUpAuthenticateForm;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final Socket socket;
    AuthenticateForm form;
    JSONObject jsonObject;

    public Menu(int port, Scanner scanner) {
        try {
            socket = new Socket("localhost", port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.scanner = scanner;
    }

    public void initMenu() {
        boolean allGood = true;
        try {
            if (allGood)
                allGood = menuEnter();
            if (allGood)
                allGood = menuRooms();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean menuEnter() throws IOException {
        boolean result;
        System.out.println("--------------------------------------------------------");
        System.out.println("1. SignIn");
        System.out.println("2. SignUp");
        System.out.println("3. Exit");
        switch (scanner.nextInt()) {
            case 1:
                result = tryAuthenticate("signIn", new SignInAuthenticateForm(scanner));
                break;
            case 2:
                result = tryAuthenticate("signUp", new SignUpAuthenticateForm(scanner));
                break;
            case 3:
                socket.close();
                result = false;
                break;
            default:
                System.out.println("Incorrect input, try again");
                result = menuEnter();
        }
        return result;
    }

    public boolean menuRooms() throws IOException {
        boolean result;
        System.out.println("--------------------------------------------------------");
        System.out.println("1. Create room");
        System.out.println("2. Choose room");
        System.out.println("3. Exit");
        switch (scanner.nextInt()) {
            case 1:
                result = tryConnectToCreateRoom("createRoom");
                break;
            case 2:
                result = tryConnectToEnterRoom("enterRoom", jsonObject);
                break;
            case 3:
                socket.close();
                result = false;
                break;
            default:
                System.out.println("Incorrect input, try again");
                result = menuRooms();
        }
        return result;
    }

    public boolean tryAuthenticate(String type, AuthenticateForm form) throws IOException {
        boolean result = true;
        String answer = ConnectToChat.request(type, form, socket);
        jsonObject = new JSONObject(answer);
        System.out.println(jsonObject.toString());
        if (!jsonObject.getString("status").equals("successfully")) {
            result = false;
        }

        return result;
    }

    public boolean tryConnectToCreateRoom(String type) throws IOException {
        boolean result = true;
        System.out.println("Enter room name:");
        String answer = ConnectCreateRoom.request(type, scanner.next(), socket);
        jsonObject = new JSONObject(answer);
        System.out.println(jsonObject.toString());
        if (!jsonObject.getString("status").equals("successfully")) {
            result = false;
        }

        return false;
    }

    public boolean tryConnectToEnterRoom(String type, JSONObject jsonObject) throws IOException {
        boolean result = true;

        System.out.println("--------------------------------------------------------");
        List<Long> rooms_ids = (List<Long>) jsonObject.get("chatRooms_ids");
        List<String> rooms_names = (List<String>) jsonObject.get("chatRooms_ids");

        int i = 0;
        for (String s: rooms_names) {
            System.out.println(++i + " " + rooms_names);
        }
        System.out.println(i + " Exit");
        System.out.println("Choose room:");
        int input = scanner.nextInt();
        if (input > i) {
            System.out.println("Incorrect input, try again");
            result = tryConnectToEnterRoom(type, jsonObject);
        } else if (i == input) {
            socket.close();
            result = false;
        }

        String answer = ConnectToRoom.request("enterRoom", rooms_ids.get(i), socket);
        jsonObject = new JSONObject(answer);
        System.out.println(jsonObject.toString());
        if (!jsonObject.getString("status").equals("successfully")) {
            result = false;
        }

        return result;
    }

    public boolean openChatRoom() {
        return false;
    }

    public void runMenu() throws IOException {
        if (authenticateMenu()) {
            startMessage();
        } else {
            System.out.println("Error");
            socket.close();
        }
    }

    public boolean authenticateMenu() {
        boolean stop = false;
        String type = "";
        while (!stop) {
            System.out.println("signUp/signIn?:");
            switch (scanner.nextLine()) {
                case "signUp":
                    type = "signUp";
                    form = new SignUpAuthenticateForm(scanner);
                    stop = true;
                    break;
                case "signIn":
                    type = "signIn";
                    form = new SignInAuthenticateForm(scanner);
                    stop = true;
                    break;
                default:
                    System.out.println("Incorrect method!");
                    break;
            }
        }

        try {
            stop = Request.request(type, form, socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stop;
    }

    public void startMessage() throws IOException {
        System.out.println("Start messaging");
        String text = "";
        final String[] str = new String[1];

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    str[0] = scanner.nextLine();
                    if (str[0].equals("exit")) {
                        break;
                    }
                }
            }
        });
        thread.start();
        do {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (str[0] != null) text = str[0];

            if (str[0] != null && !str[0].equals("")) {
//                System.out.println("Out message");
                SendMessage.request(form.getInputValues().get("Login"), str[0], socket);
                str[0] = "";
            }

            if (in.ready()) {
                String input = in.readLine();
                String[] splitInput = input.split("\\|");
                if (splitInput[0].equals("message") && !splitInput[1].equals(form.getInputValues().get("Login")))
                    System.out.println(splitInput[1] + ":" + splitInput[2]);
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (!text.equals("exit"));
        socket.close();
    }
}
