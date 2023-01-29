package com.example.walletservice.service;

import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;

public interface WalletService {
    void createWallet(Long userId);
    Wallet getWalletById(Long id) throws WalletNotFoundException;
    Wallet getWalledByUserId(Long userId) throws WalletNotFoundException;
    Wallet depositToWalletByUserId(Long userId, Double value) throws WalletNotFoundException;
    Wallet depositToWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException;
    Wallet payFromWalletByWalletId(Long userId, Double value) throws NotEnoughValueException, WalletNotFoundException;
    Wallet payFromWalletByUserId(Long wallet, Double value) throws WalletNotFoundException, NotEnoughValueException;
}
