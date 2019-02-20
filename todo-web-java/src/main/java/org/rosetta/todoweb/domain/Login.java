package org.rosetta.todoweb.domain;

public class Login {
    public Login() { }

    public Login(String password, String clientId) {
        this.password = password;
        this.clientId = clientId;
    }

    private String password;
    private String clientId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
