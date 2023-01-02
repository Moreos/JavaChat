package com.moreos.client.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.moreos.client.menu.Menu;

import java.io.*;
import java.util.Scanner;

@Parameters(separators = "=")

public class Main {
    @Parameter(names = {"-p", "--port"},
            description = "port for host",
            required = true)
    int port;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }

    public void run() throws IOException {;
        Scanner scanner = new Scanner(System.in);

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("Aboba", 123123);
//        jsonObject.put("Hoba", "dddddd");
//        String str = jsonObject.toString();
//        System.out.println(str);
//        JSONObject jsonObject1 = new JSONObject(str);
//        System.out.println(jsonObject1.toString());
//        System.out.println(jsonObject1.getString("Hoba"));

        Menu menu = new Menu(port, scanner);
        menu.initMenu();
//        menu.runMenu();
    }
}
