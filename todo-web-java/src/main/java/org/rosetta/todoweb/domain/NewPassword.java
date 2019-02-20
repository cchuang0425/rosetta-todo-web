package org.rosetta.todoweb.domain;

public class NewPassword {
    private Session session;
    private String password;

    public NewPassword() { }

    public NewPassword(Session session, String password) {
        this.session = session;
        this.password = password;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
