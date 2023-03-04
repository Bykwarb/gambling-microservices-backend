package com.example.walletservice.service;

import com.example.userservice.utils.ClientContextHolder;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class DefaultWithdrawService implements WithdrawService{
    private final WalletRepository walletRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public DefaultWithdrawService(WalletRepository walletRepository, SimpMessagingTemplate messagingTemplate) {
        this.walletRepository = walletRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Wallet payFromWalletByUserName(String userName, Double value) throws WalletNotFoundException, NotEnoughValueException {
        Wallet wallet = walletRepository.getWalletByUserName(userName);
        if (Objects.isNull(wallet)) {
            throw new WalletNotFoundException("Wallet not found");
        }
        if (value > wallet.getValue()) {
            log.debug("Not enough money to complete transaction. User-id: {}. Value: {}. Correlation-id: {}", userName, value,  ClientContextHolder.getContext().getCorrelationId());
            throw new NotEnoughValueException("Insufficient funds to complete the transaction");
        }
        wallet.setValue(wallet.getValue() - value);
        walletRepository.save(wallet);
        messagingTemplate.convertAndSendToUser(userName, "/balance", wallet.getValue());
        return wallet;
    }

    @Override
    public Wallet payFromWalletByWalletId(Long walletId, Double value) throws NotEnoughValueException, WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()) {
            throw new WalletNotFoundException("Wallet not found");
        }
        Wallet wallet = optionalWallet.get();
        if (value > wallet.getValue()) {
            log.debug("Not enough money to complete transaction. Wallet-id: {}. Value: {}. Correlation-id: {}", walletId, value,  ClientContextHolder.getContext().getCorrelationId());
            throw new NotEnoughValueException("Insufficient funds to complete the transaction");
        }
        wallet.setValue(wallet.getValue() - value);
        walletRepository.save(wallet);
        return wallet;
    }
}
