package com.moreos.sockets.services;

import com.moreos.sockets.models.User;
import com.moreos.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersServiceImpl implements UsersService {
    UsersRepository usersRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(String login, String password) {
        boolean userResult = true;

        Optional<User> user = usersRepository.findByLogin(login);
        if (!user.isPresent()) {
            usersRepository.save(new User(null, login, passwordEncoder.encode(password), false));
        } else {
            userResult = false;
        }

        return userResult;
    }

    @Override
    public User authenticate(String login, String password) {
        User userResult = null;

        Optional<User> user = usersRepository.findByLogin(login);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            user.get().setAuthenticationStatus(true);
            userResult = usersRepository.update(user.get());
        }

        return userResult;
    }

    @Override
    public void sessionClose(User user) {
        user.setAuthenticationStatus(false);
        usersRepository.update(user);
    }
}
