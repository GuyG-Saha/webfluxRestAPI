package com.reactive.webflux2.exception;

public class CreditCardNotFoundException extends RuntimeException{
    private String message;

    public CreditCardNotFoundException(String message){
        super(message);
        this.message = message;
    }
}
