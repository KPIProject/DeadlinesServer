package com.ddanilyuk.DeadlinesServer.extensions;

/**
 * Вивід помилок
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String error) {
        super(error);
    }


    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

