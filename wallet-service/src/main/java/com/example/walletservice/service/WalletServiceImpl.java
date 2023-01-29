package com.example.walletservice.service;

import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import com.example.walletservice.utils.WalletContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public void createWallet(Long userId) {
        Wallet wallet = new Wallet(userId);
        walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(Long id) throws WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);
        if (optionalWallet.isEmpty()) {
            logger.debug("Wallet not found. Wallet-id: {}. Correlation-id: {}", id, WalletContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        return optionalWallet.get();
    }

    @Override
    public Wallet getWalledByUserId(Long userId) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserId(userId);
        if (Objects.isNull(wallet)) {
            logger.debug("Wallet not found. User-id: {}. Correlation-id: {}", userId, WalletContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        return wallet;
    }

    @Override
    public Wallet depositToWalletByUserId(Long userId, Double value) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserId(userId);
        if (Objects.isNull(wallet)) {
            logger.debug("Wallet not found. User-id: {}. Correlation-id: {}", userId, WalletContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        wallet.setValue(wallet.getValue() + value);
        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public Wallet depositToWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()) {
            logger.debug("Wallet not found. Wallet-id: {}. Correlation-id: {}", walletId, WalletContextHolder.getContext().getCorrelationId());
            throw new WalletNotFoundException("Wallet not founded");
        }
        Wallet wallet = optionalWallet.get();
        wallet.setValue(wallet.getValue() + value);
        walletRepository.save(wallet);
        return wallet;
    }


    @Override
    public Wallet payFromWalletByUserId(Long userId, Double value) throws WalletNotFoundException, NotEnoughValueException {
        Wallet wallet = walletRepository.getWalletByUserId(userId);
        if (Objects.isNull(wallet)) {
            throw new WalletNotFoundException("Wallet not founded");
        }
        if (value > wallet.getValue()) {
            logger.debug("Not enough money to complete transaction. User-id: {}. Value: {}. Correlation-id: {}", userId, value, WalletContextHolder.getContext().getCorrelationId());
            throw new NotEnoughValueException("Insufficient funds to complete the transaction");
        }
        wallet.setValue(wallet.getValue() - value);
        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public Wallet payFromWalletByWalletId(Long walletId, Double value) throws NotEnoughValueException, WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()) {
            throw new WalletNotFoundException("Wallet not founded");
        }
        Wallet wallet = optionalWallet.get();
        if (value > wallet.getValue()) {
            logger.debug("Not enough money to complete transaction. Wallet-id: {}. Value: {}. Correlation-id: {}", walletId, value, WalletContextHolder.getContext().getCorrelationId());
            throw new NotEnoughValueException("Insufficient funds to complete the transaction");
        }
        wallet.setValue(wallet.getValue() - value);
        walletRepository.save(wallet);
        return wallet;
    }
}
