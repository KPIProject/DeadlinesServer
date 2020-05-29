package com.ddanilyuk.DeadlinesServer.extensions;

/**
 * Вивід повідомлення про успішне виконання
 */
public class SuccessException extends RuntimeException {

    public SuccessException(String message) {
        super(message);
    }
}
