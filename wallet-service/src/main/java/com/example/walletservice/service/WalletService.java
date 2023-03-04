package com.example.walletservice.service;

import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;

public interface WalletService {
    void createWallet(String username);
    Wallet getWalletById(Long id) throws WalletNotFoundException;
    Wallet getWalledByUserName(String userName) throws WalletNotFoundException;
}
