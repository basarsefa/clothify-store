package com.sefa.clothify_store.exception;

public class EmailMessagingException extends RuntimeException{
    public EmailMessagingException (String message) {
        super(message);
    }

    public EmailMessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
