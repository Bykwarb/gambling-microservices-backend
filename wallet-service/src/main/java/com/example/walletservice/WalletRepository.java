package com.example.walletservice;

import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Wallet getWalletByUserId(Long userId);

}
