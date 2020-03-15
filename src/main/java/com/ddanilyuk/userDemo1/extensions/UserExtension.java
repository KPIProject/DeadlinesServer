package com.ddanilyuk.userDemo1.extensions;

public class UserExtension extends RuntimeException {

    public UserExtension(String username) {
        super(username);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

