package com.example.walletservice.service;

import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import com.example.walletservice.service.DefaultDepositService;
import com.example.walletservice.service.DefaultWalletService;
import com.example.walletservice.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class DefaultWalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletService walletService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("testuser");
        walletService = new DefaultWalletService(walletRepository);
        wallet.setValue(100.0);
        when(walletRepository.getWalletByUserName("testuser")).thenReturn(wallet);
        when(walletRepository.findById(1l)).thenReturn(Optional.of(wallet));
    }

    @Test
    public void createWalletTest(){
        walletService.createWallet("testuser");
        verify(walletRepository, times(1)).save(notNull());
    }

    @Test
    public void getWalletByUsername() throws WalletNotFoundException {
        Wallet wallet1 = walletService.getWalledByUserName("testuser");
        Wallet wallet2 = new Wallet("testuser");
        wallet2.setValue(100.0);
        wallet2.setWalletId(1l);
        assertEquals(wallet1.getUserName(), wallet2.getUserName());
        assertEquals(wallet1.getValue(), wallet2.getValue());
        assertEquals(wallet1.getWalletId(), wallet2.getWalletId());
    }


    @Test
    public void getWalletById() throws WalletNotFoundException {
        Wallet wallet1 = walletService.getWalletById(1l);
        Wallet wallet2 = new Wallet("testuser");
        wallet2.setValue(100.0);
        wallet2.setWalletId(1l);
        assertEquals(wallet1.getUserName(), wallet2.getUserName());
        assertEquals(wallet1.getValue(), wallet2.getValue());
        assertEquals(wallet1.getWalletId(), wallet2.getWalletId());
    }
}
