package com.ddanilyuk.userDemo1.extensions;

public class ServiceException extends RuntimeException {

    public ServiceException(String error) {
        super(error);
    }


    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

