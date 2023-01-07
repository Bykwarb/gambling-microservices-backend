package com.example.walletservice;

import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

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
        if (optionalWallet.isEmpty()){
            throw new WalletNotFoundException("Wallet not founded");
        }
        return optionalWallet.get();
    }

    @Override
    public Wallet getWalledByUserId(Long userId) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserId(userId);
        if (Objects.isNull(wallet)){
            throw new WalletNotFoundException("Wallet not founded");
        }
        return wallet;
    }

    @Override
    public Wallet depositToWalletByUserId(Long userId, Double value) throws WalletNotFoundException {
        Wallet wallet = walletRepository.getWalletByUserId(userId);
        if (Objects.isNull(wallet)){
            throw new WalletNotFoundException("Wallet not founded");
        }
        wallet.setValue(wallet.getValue() + value);
        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public Wallet depositToWalletByWalletId(Long walletId, Double value) throws WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()){
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
        if (Objects.isNull(wallet)){
            throw new WalletNotFoundException("Wallet not founded");
        }
        if (value > wallet.getValue()){
            throw new NotEnoughValueException("Insufficient funds to complete the transaction");
        }
        wallet.setValue(wallet.getValue() - value);
        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public Wallet payFromWalletByWalletId(Long walletId, Double value) throws NotEnoughValueException, WalletNotFoundException {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isEmpty()){
            throw new WalletNotFoundException("Wallet not founded");
        }
        Wallet wallet = optionalWallet.get();
        if (value > wallet.getValue()){
            throw new NotEnoughValueException("Insufficient funds to complete the transaction");
        }
        wallet.setValue(wallet.getValue() - value);
        walletRepository.save(wallet);
        return wallet;
    }
}
