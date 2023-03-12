package com.example.walletservice.service;

import com.example.userservice.utils.ClientContextHolder;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class DefaultDepositService implements DepositService{
    private final WalletRepository walletRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public DefaultDepositService(WalletRepository walletRepository, SimpMessagingTemplate messagingTemplate) {
        this.walletRepository = walletRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public Wallet depositToWalletByUserName(String userName, Double value) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserName(userName);
        if (wallet == null) {
            log.debug("Wallet not found. User-id: {}. Correlation-id: {}", userName, ClientContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not found");
        }
        wallet.setValue(wallet.getValue() + value);
        walletRepository.save(wallet);
        messagingTemplate.convertAndSendToUser(userName, "/balance", wallet.getValue());
        return wallet;
    }

    @Override
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    public Wallet depositToWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> {
            log.debug("Wallet not found. Wallet-id: {}. Correlation-id: {}", walletId, ClientContextHolder.getContext().getCorrelationId());
            return new WalletNotFoundException("Wallet not found");
        });
        wallet.setValue(wallet.getValue() + value);
        walletRepository.save(wallet);
        messagingTemplate.convertAndSendToUser(wallet.getUserName(), "/balance", wallet.getValue());
        return wallet;
    }
}
