package com.moreos.client.forms;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class SignInAuthenticateForm implements AuthenticateForm {
    private final Map<String, String> inputValues;
    Scanner scanner;

    public SignInAuthenticateForm(Scanner scanner){
        this.scanner = scanner;
        inputValues = new HashMap<>();
    }

    @Override
    public Map<String, String> getInputValues() {
        return inputValues;
    }

    @Override
    public Map<String, String> enterForm() {
        inputValues.put("login", inputLogin());
        inputValues.put("password", inputPassword());
        return inputValues;
    }

    @Override
    public String inputLogin() {
        System.out.println("Enter login:");
        return scanner.next();
    }

    @Override
    public String inputPassword() {
        System.out.println("Enter password:");
        return scanner.next();
    }
}
