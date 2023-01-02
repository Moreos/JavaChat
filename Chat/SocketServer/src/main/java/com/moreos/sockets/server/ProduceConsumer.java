package com.moreos.sockets.server;

import com.moreos.sockets.models.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProduceConsumer {

    private static final int BUFFER_MAX_SIZE = 32;
    private Map<Long, Message> bufferMap = new HashMap<>();

    synchronized void produce(Long room_id, Message message) throws InterruptedException {
        while (bufferMap.size() == BUFFER_MAX_SIZE) {
            wait();
        }
        System.out.println("write from " + message.getAuthor().getLogin() + " " + message.getText());
        if (bufferMap.get(room_id) != null) {
            bufferMap.replace(room_id, message);
        } else {
            bufferMap.put(room_id, message);
        }
        notify();
    }

    synchronized Message consume(Long room_id) throws InterruptedException {
        while (bufferMap.size() == 0) {
            notify();
            return null;
        }
        notify();
        return bufferMap.get(room_id);
    }
}
