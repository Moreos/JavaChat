package com.moreos.sockets.services;

import com.moreos.sockets.models.User;

public interface UsersService {
    User authenticate(String login, String password);
    boolean register(String login, String password);
    void sessionClose(User user);
}
