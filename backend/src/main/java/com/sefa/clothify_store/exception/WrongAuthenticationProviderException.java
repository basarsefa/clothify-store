package com.sefa.clothify_store.exception;

public class WrongAuthenticationProviderException extends RuntimeException{

    public WrongAuthenticationProviderException(String message) {
        super(message);
    }
}
