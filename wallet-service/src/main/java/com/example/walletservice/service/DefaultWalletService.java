package com.example.walletservice.service;

import com.example.userservice.utils.ClientContextHolder;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class DefaultWalletService implements WalletService {
    private final WalletRepository walletRepository;
    public DefaultWalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "fallbackCreateWallet")
    public void createWallet(String userName) {
        Wallet wallet = new Wallet(userName);
        walletRepository.save(wallet);
    }

    @Override
    @Retry(name = "default")
    public Wallet getWalletById(Long id) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() ->{
            log.debug("Wallet not found. Wallet-id: {}. Correlation-id: {}", id,  ClientContextHolder.getContext().getCorrelationId());
            return new WalletNotFoundException("Wallet not founded");
        });
        return wallet;
    }

    @Override
    @Retry(name = "default")
    public Wallet getWalledByUserName(String userName) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserName(userName);
        if (wallet == null) {
            log.debug("Wallet not found. User-id: {}. Correlation-id: {}", userName,  ClientContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        return wallet;
    }

    private void fallbackCreateWallet(String userName, Throwable t) {
        log.error("Error occurred while creating wallet for user {} : {}", userName, t.getMessage());
    }

}
