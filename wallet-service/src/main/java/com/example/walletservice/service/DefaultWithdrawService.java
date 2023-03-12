package com.example.walletservice.service;

import com.example.userservice.utils.ClientContextHolder;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class DefaultWithdrawService implements WithdrawService{
    private final WalletRepository walletRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public DefaultWithdrawService(WalletRepository walletRepository, SimpMessagingTemplate messagingTemplate) {
        this.walletRepository = walletRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public Wallet payFromWalletByUserName(String userName, Double value) throws WalletNotFoundException, NotEnoughValueException {
        Wallet wallet = walletRepository.getWalletByUserName(userName);
        if (wallet == null) {
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
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public Wallet payFromWalletByWalletId(Long walletId, Double value) throws NotEnoughValueException, WalletNotFoundException {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> {
            log.debug("Wallet not found. Wallet-id: {}. Correlation-id: {}", walletId, ClientContextHolder.getContext().getCorrelationId());
            return new WalletNotFoundException("Wallet not found");
        });
        if (value > wallet.getValue()) {
            log.debug("Not enough money to complete transaction. Wallet-id: {}. Value: {}. Correlation-id: {}", walletId, value,  ClientContextHolder.getContext().getCorrelationId());
            throw new NotEnoughValueException("Insufficient funds to complete the transaction");
        }
        wallet.setValue(wallet.getValue() - value);
        walletRepository.save(wallet);
        messagingTemplate.convertAndSendToUser(wallet.getUserName(), "/balance", wallet.getValue());
        return wallet;
    }
}
