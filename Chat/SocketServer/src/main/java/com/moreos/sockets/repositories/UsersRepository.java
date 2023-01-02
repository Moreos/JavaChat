package com.moreos.sockets.repositories;

import com.moreos.sockets.models.User;

import java.util.Optional;

public interface UsersRepository {
    Optional<User> findByLogin(String login);
    void save(User user);
    User update(User user);
}
