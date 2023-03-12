package com.example.walletservice.service;

import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import com.example.walletservice.service.DefaultDepositService;
import com.example.walletservice.service.DefaultWithdrawService;
import com.example.walletservice.service.DepositService;
import com.example.walletservice.service.WithdrawService;
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

public class DefaultWithdrawServiceTestById {
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private WithdrawService withdrawService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        withdrawService = new DefaultWithdrawService(walletRepository, messagingTemplate);
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setValue(100.0);
        wallet.setUserName("test");
        when(walletRepository.findById(1l)).thenReturn(Optional.of(wallet));
    }

    @Test
    public void testPayFromWalletById() throws WalletNotFoundException, NotEnoughValueException {
        Double value = 50.0;
        Long id = 1l;
        when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Wallet result = withdrawService.payFromWalletByWalletId(id,value);
        assertEquals(Double.valueOf(50.0), result.getValue());
        Wallet wallet = walletRepository.findById(1l).get();
        assertNotNull(wallet);
        assertEquals(Double.valueOf(50.0), wallet.getValue());
        verify(messagingTemplate, times(1)).convertAndSendToUser(eq(wallet.getUserName()), eq("/balance"), eq(50.0));
    }
    @Test
    public void testPayFromWalletByIdButNotEnoughValue() throws WalletNotFoundException, NotEnoughValueException {
        Double value = 400.0;
        Long id = 1l;
        when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        assertThrows(NotEnoughValueException.class, ()->{
            Wallet result = withdrawService.payFromWalletByWalletId(id,value);
        });
    }

    @Test
    public void testPayFromNonexistentWalletById() {
        Double value = 50.0;
        Long userId = 2l;
        assertThrows(WalletNotFoundException.class, () -> {
            withdrawService.payFromWalletByWalletId(userId,value);
        });
        verify(walletRepository, times(1)).findById(2l);
        verifyNoInteractions(messagingTemplate);
    }
}
