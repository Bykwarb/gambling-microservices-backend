package com.example.walletservice.service;

import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.exception.WalletNotFoundException;

public interface WithdrawService {
    Wallet payFromWalletByUserName(String userName, Double value) throws WalletNotFoundException, NotEnoughValueException;
    Wallet payFromWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException, NotEnoughValueException;
}
