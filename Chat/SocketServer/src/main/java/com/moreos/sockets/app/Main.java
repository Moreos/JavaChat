package com.moreos.sockets.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.moreos.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Parameters(separators = "=")

public class Main {
    @Parameter(names = {"-p", "--port"},
            description = "port for host",
            required = true)
    int port;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }

    public void run() {
        ApplicationContext context = new AnnotationConfigApplicationContext("edu.school21.sockets");
        Server server = context.getBean(Server.class);
        server.run(port);
    }
}
