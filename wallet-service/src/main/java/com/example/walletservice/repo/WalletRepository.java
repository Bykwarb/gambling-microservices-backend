package com.example.walletservice.repo;

import com.example.walletservice.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Wallet getWalletByUserName(String username);

}
