package com.example.walletservice.service;

import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import com.example.walletservice.service.DefaultDepositService;
import com.example.walletservice.service.DepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultDepositServiceTestById {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private DepositService depositService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        depositService = new DefaultDepositService(walletRepository, messagingTemplate);
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setValue(100.0);
        wallet.setUserName("test");
        when(walletRepository.findById(1l)).thenReturn(Optional.of(wallet));
    }

    @Test
    public void testDepositToWalletById() throws WalletNotFoundException {
        Double value = 50.0;
        Long id = 1l;
        when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Wallet result = depositService.depositToWalletByWalletId(id, value);
        assertEquals(Double.valueOf(150.0), result.getValue());
        Wallet wallet = walletRepository.findById(1l).get();
        assertNotNull(wallet);
        assertEquals(Double.valueOf(150.0), wallet.getValue());
        verify(messagingTemplate, times(1)).convertAndSendToUser(eq(wallet.getUserName()), eq("/balance"), eq(150.0));
    }

    @Test
    public void testDepositToNonexistentWalletById() {
        Double value = 50.0;
        Long userId = 2l;
        assertThrows(WalletNotFoundException.class, () -> {
            depositService.depositToWalletByWalletId(userId, value);
        });
        verify(walletRepository, times(1)).findById(2l);
        verifyNoInteractions(messagingTemplate);
    }


}
