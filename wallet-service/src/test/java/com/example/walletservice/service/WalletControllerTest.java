package com.example.walletservice.service;
import com.example.userservice.entities.WalletDto;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.repo.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    private final String url = "/v1/wallet/";
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCeWt3YXJiIiwiZW1haWwiOiJuZWJ5a3dhcmJAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJrZXkiOiIkMmEkMTAkRWZyZWxQcHlvNGUxMmdBcEswQUIvdXBuS0MzeElBQmthRjVkV3ZIUmJaLmprR1AzZHZTT3EiLCJpYXQiOjE2Nzg2MDQ3MTgsImV4cCI6MTY3ODY5MTExOH0.4fmpX9IgwGHz-j_msM12niUULMWde2N6TAo3PGNNgu4";
    private final String header = "Authorization";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalletService walletService;
    @MockBean
    private DepositService depositService;
    @MockBean
    private WalletRepository repository;
    @MockBean
    private WithdrawService withdrawService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createWallet_shouldReturnOk() throws Exception {
        WalletDto walletDto = new WalletDto();
        walletDto.setUserName("test");
        doNothing().when(walletService).createWallet(anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(url + "create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wallet successfully created\"}"));
        verify(walletService, times(1)).createWallet(anyString());
    }

    @Test
    public void getWalletByUsername_shouldReturnOk() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setUserName("Bykwarb");
        wallet.setValue(100.0);
        wallet.setWalletId(1l);
        when(walletService.getWalledByUserName("Bykwarb")).thenReturn(wallet);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url + "get/user-name/")
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Wallet responseWallet = mapper.readValue(content, Wallet.class);
        assertEquals(wallet.getWalletId(), responseWallet.getWalletId());
        assertEquals(wallet.getValue(), responseWallet.getValue());
        assertEquals(wallet.getUserName(), responseWallet.getUserName());
        verify(walletService, times(1)).getWalledByUserName("Bykwarb");
    }

    @Test
    public void getWalletByUsername_shouldReturnNotFound() throws Exception {
        when(walletService.getWalledByUserName(anyString())).thenThrow(new WalletNotFoundException(""));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "get/user-name/")
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wallet not found\"}"));
        verify(walletService, times(1)).getWalledByUserName("Bykwarb");
    }

    @Test
    public void getWalletById_shouldReturnOk() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(walletService.getWalletById(1l)).thenReturn(wallet);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url + "get/wallet-id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Wallet responseWallet = mapper.readValue(content, Wallet.class);
        assertEquals(wallet.getWalletId(), responseWallet.getWalletId());
        assertEquals(wallet.getValue(), responseWallet.getValue());
        assertEquals(wallet.getUserName(), responseWallet.getUserName());
        verify(walletService, times(1)).getWalletById(1l);
    }

    @Test
    public void getWalletById_shouldReturnNotFound() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(walletService.getWalletById(1l)).thenThrow(new WalletNotFoundException(""));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "get/wallet-id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wallet not found\"}"));
        verify(walletService, times(1)).getWalletById(1l);
    }

    @Test
    public void successfulDepositById() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(depositService.depositToWalletByWalletId(1l, 400.0)).thenAnswer(invocation -> {
            Long walletId = invocation.getArgument(0, Long.class);
            Double value = invocation.getArgument(1, Double.class);
            wallet.setWalletId(walletId);
            wallet.setUserName("Test");
            wallet.setValue(wallet.getValue() + value);
            return wallet;
        });
        when(repository.findById(1l)).thenReturn(Optional.of(wallet));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "deposit/wallet-id/1?value=400")
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(depositService, times(1)).depositToWalletByWalletId(1l, 400.0);
    }
    @Test
    public void successfulDepositByUserName() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(depositService.depositToWalletByUserName("Test", 400.0)).thenAnswer(invocation -> {
            Double value = invocation.getArgument(1, Double.class);
            wallet.setValue(wallet.getValue() + value);
            return wallet;
        });
        when(repository.getWalletByUserName("Test")).thenReturn(wallet);
        mockMvc.perform(MockMvcRequestBuilders.put(url + "deposit/user-name/Test?value=400")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(depositService, times(1)).depositToWalletByUserName("Test", 400.0);
    }

    @Test
    public void depositByIdButWalletNotFound() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(depositService.depositToWalletByWalletId(1l, 400.0)).thenThrow(new WalletNotFoundException(""));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "deposit/wallet-id/1?value=400")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wallet not found\"}"));
        verify(depositService, times(1)).depositToWalletByWalletId(1l, 400.0);
    }
    @Test
    public void depositByUserNameButWalletNotFound() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(depositService.depositToWalletByUserName("Test", 400.0)).thenThrow(new WalletNotFoundException(""));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "deposit/user-name/Test?value=400")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wallet not found\"}"));
        verify(depositService, times(1)).depositToWalletByUserName("Test", 400.0);
    }

    @Test
    public void successfulWithdrawById() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(withdrawService.payFromWalletByWalletId(1l, 50.0)).thenAnswer(invocationOnMock -> {
            Double value = invocationOnMock.getArgument(1, Double.class);
            wallet.setValue(wallet.getValue() - value);
            return wallet;
        });
        when(repository.findById(1l)).thenReturn(Optional.of(wallet));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "debited/wallet-id/1?debited-value=50")
                .contentType(MediaType.APPLICATION_JSON)
                .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(withdrawService, times(1)).payFromWalletByWalletId(1l, 50.0);
    }

    @Test
    public void successfulWithdrawByUsername() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(1l);
        wallet.setUserName("Test");
        wallet.setValue(100.0);
        when(withdrawService.payFromWalletByUserName("Test", 50.0)).thenAnswer(invocationOnMock -> {
            Double value = invocationOnMock.getArgument(1, Double.class);
            wallet.setValue(wallet.getValue() - value);
            return wallet;
        });
        when(repository.getWalletByUserName("Test")).thenReturn(wallet);
        mockMvc.perform(MockMvcRequestBuilders.put(url + "debited/user-name/Test?debited-value=50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(withdrawService, times(1)).payFromWalletByUserName("Test", 50.0);
    }

    @Test
    public void withdrawByIdButNotEnoughValue() throws Exception {
        when(withdrawService.payFromWalletByWalletId(any(), anyDouble())).thenThrow(new NotEnoughValueException(""));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "debited/wallet-id/1?debited-value=50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"There are not enough funds on the wallet to complete the transaction\"}"));
        verify(withdrawService, times(1)).payFromWalletByWalletId(1l, 50.0);
    }

    @Test
    public void withdrawByUsernameButNotEnoughValue() throws Exception {
        when(withdrawService.payFromWalletByUserName(anyString(), anyDouble())).thenThrow(new NotEnoughValueException(""));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "debited/user-name/Test?debited-value=50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"There are not enough funds on the wallet to complete the transaction\"}"));
        verify(withdrawService, times(1)).payFromWalletByUserName("Test", 50.0);
    }

    @Test
    public void withdrawByIdButWalletNotFound() throws Exception {
        when(withdrawService.payFromWalletByWalletId(any(), anyDouble())).thenThrow(new WalletNotFoundException(""));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "debited/wallet-id/1?debited-value=50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wallet not found\"}"));
        verify(withdrawService, times(1)).payFromWalletByWalletId(1l, 50.0);
    }

    @Test
    public void withdrawByUsernameButWalletNotFound() throws Exception {
        when(withdrawService.payFromWalletByUserName(anyString(), anyDouble())).thenThrow(new WalletNotFoundException(""));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "debited/user-name/Test?debited-value=50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wallet not found\"}"));
        verify(withdrawService, times(1)).payFromWalletByUserName("Test", 50.0);
    }

}
