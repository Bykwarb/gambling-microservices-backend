package com.example.walletservice;

public class NotEnoughValueException extends Exception{
    public NotEnoughValueException(String message) {
        super(message);
    }
}
