package com.moreos.client.forms;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SignUpAuthenticateForm implements AuthenticateForm {
    Map<String, String> inputValues;
    Scanner scanner;

    public SignUpAuthenticateForm(Scanner scanner){
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
        return scanner.nextLine();
    }

    @Override
    public String inputPassword() {
        System.out.println("Enter password:");
        String password = scanner.next();
        System.out.println("Confirm password:");
        if (!scanner.next().equals(password)) {
            System.out.println("Password didn't match, try again");
            password = inputPassword();
        }
        return password;
    }
}
