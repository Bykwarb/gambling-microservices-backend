package com.example.walletservice;

public class WalletNotFoundException extends Exception{
    public WalletNotFoundException(String message) {
        super(message);
    }
}
