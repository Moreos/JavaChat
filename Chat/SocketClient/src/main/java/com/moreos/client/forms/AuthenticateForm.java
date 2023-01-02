package com.moreos.client.forms;

import java.util.Map;

public interface AuthenticateForm {
    public Map<String, String> getInputValues();
    public Map<String, String> enterForm();
    public String inputLogin();
    public String inputPassword();
}
