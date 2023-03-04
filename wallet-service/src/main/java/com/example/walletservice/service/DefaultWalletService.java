package com.example.walletservice.service;

import com.example.userservice.utils.ClientContextHolder;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class DefaultWalletService implements WalletService {
    private final WalletRepository walletRepository;
    private final SimpMessagingTemplate messagingTemplate;
    public DefaultWalletService(WalletRepository walletRepository, SimpMessagingTemplate messagingTemplate) {
        this.walletRepository = walletRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void createWallet(String userName) {
        Wallet wallet = new Wallet(userName);
        walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(Long id) throws WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);
        if (optionalWallet.isEmpty()) {
            log.debug("Wallet not found. Wallet-id: {}. Correlation-id: {}", id,  ClientContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        return optionalWallet.get();
    }

    @Override
    public Wallet getWalledByUserName(String userName) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserName(userName);
        if (Objects.isNull(wallet)) {
            log.debug("Wallet not found. User-id: {}. Correlation-id: {}", userName,  ClientContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        return wallet;
    }

}
