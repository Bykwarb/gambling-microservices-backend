package com.example.walletservice.service;

import com.example.userservice.utils.ClientContextHolder;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
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
    public Wallet depositToWalletByUserName(String userName, Double value) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserName(userName);
        if (Objects.isNull(wallet)) {
            log.debug("Wallet not found. User-id: {}. Correlation-id: {}", userName, ClientContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        wallet.setValue(wallet.getValue() + value);
        walletRepository.save(wallet);
        messagingTemplate.convertAndSendToUser(userName, "/balance", wallet.getValue());
        return wallet;
    }

    @Override
    public Wallet depositToWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()) {
            log.debug("Wallet not found. Wallet-id: {}. Correlation-id: {}", walletId, ClientContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        Wallet wallet = optionalWallet.get();
        wallet.setValue(wallet.getValue() + value);
        walletRepository.save(wallet);
        return wallet;
    }
}
