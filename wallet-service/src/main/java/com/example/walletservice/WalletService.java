package com.example.walletservice;

public interface WalletService {
    void createWallet(Long userId);
    Wallet getWalletById(Long id) throws WalletNotFoundException;
    Wallet getWalledByUserId(Long userId) throws WalletNotFoundException;
    Wallet depositToWalletByUserId(Long userId, Double value) throws WalletNotFoundException;
    Wallet depositToWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException;
    Wallet payFromWalletByWalletId(Long userId, Double value) throws NotEnoughValueException, WalletNotFoundException;
    Wallet payFromWalletByUserId(Long wallet, Double value) throws WalletNotFoundException, NotEnoughValueException;
}
