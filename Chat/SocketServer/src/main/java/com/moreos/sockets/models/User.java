package com.moreos.sockets.models;


public class User {
    private Long id;
    private String login;
    private String password;
    private boolean AuthenticationStatus;

    public User(Long id, String login, String password, boolean authenticationStatus) {
        this.id = id;
        this.login = login;
        this.password = password;
        AuthenticationStatus = authenticationStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthenticationStatus() {
        return AuthenticationStatus;
    }

    public void setAuthenticationStatus(boolean authenticationStatus) {
        AuthenticationStatus = authenticationStatus;
    }
}
