package com.example.walletservice.service;

import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;

public interface DepositService {
    Wallet depositToWalletByUserName(String userName, Double value) throws WalletNotFoundException;
    Wallet depositToWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException;
}
