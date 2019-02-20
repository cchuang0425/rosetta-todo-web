package org.rosetta.todoweb.domain;

public abstract class Response {
    public static final String KEY_SUCCEED = "succeed";
    public static final String KEY_MESSAGE = "message";

    protected boolean succeed;
    protected String message;

    protected Response(boolean succeed) {
        this.succeed = succeed;
    }

    protected Response(boolean succeed, String message) {
        this.succeed = succeed;
        this.message = message;
    }

    public boolean isSucceed() { return succeed; }

    public String getMessage() {
        return message;
    }

    public static Response getResponse(boolean succeed) {
        return succeed ? Succeed.getInstance() : Failed.getInstance();
    }

    public static Response getResponse(boolean succeed, String message) {
        return succeed ? Succeed.getInstance(message) : Failed.getInstance(message);
    }

    public static class Succeed extends Response {
        public static Succeed getInstance() {
            return new Succeed(true);
        }

        public static Succeed getInstance(String message) {
            return new Succeed(true, message);
        }

        public Succeed(boolean succeed) {
            super(succeed);
        }

        public Succeed(boolean succeed, String message) {
            super(succeed, message);
        }
    }

    public static class Failed extends Response {
        public static Failed getInstance(String message) {
            return new Failed(false, message);
        }

        public static Failed getInstance() {
            return new Failed(false);
        }

        public Failed(boolean succeed, String message) {
            super(succeed, message);
        }

        public Failed(boolean succeed) {
            super(succeed);
        }
    }
}
