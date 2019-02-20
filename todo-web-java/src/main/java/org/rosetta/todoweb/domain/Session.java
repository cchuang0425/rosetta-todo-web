package org.rosetta.todoweb.domain;

public class Session {
    public Session() {}

    public Session(String token, String clientId) {
        this.token = token;
        this.clientId = clientId;
    }

    private String token;
    private String clientId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
