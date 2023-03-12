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

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultDepositServiceTestByName {

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
        wallet.setUserName("testuser");
        wallet.setValue(100.0);
        when(walletRepository.getWalletByUserName("testuser")).thenReturn(wallet);
    }

    @Test
    public void testDepositToWalletByUserName() throws WalletNotFoundException {
        Double value = 50.0;
        String userName = "testuser";
        when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Wallet result = depositService.depositToWalletByUserName(userName, value);
        assertEquals(Double.valueOf(150.0), result.getValue());
        Wallet wallet = walletRepository.getWalletByUserName(userName);
        assertNotNull(wallet);
        assertEquals(Double.valueOf(150.0), wallet.getValue());
        verify(messagingTemplate, times(1)).convertAndSendToUser(eq(userName), eq("/balance"), eq(150.0));
    }

    @Test
    public void testDepositToNonexistentWallet() {
        Double value = 50.0;
        String userName = "testuser";
        when(walletRepository.getWalletByUserName(userName)).thenReturn(null);

        assertThrows(WalletNotFoundException.class, () -> {
            depositService.depositToWalletByUserName(userName, value);
        });
        verify(walletRepository, times(1)).getWalletByUserName(userName);
        verifyNoInteractions(messagingTemplate);
    }


}
