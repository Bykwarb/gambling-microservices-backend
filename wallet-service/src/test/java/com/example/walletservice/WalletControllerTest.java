package com.example.walletservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class WalletControllerTest extends AbstractTest{
    @Autowired
    ObjectMapper objectMapper;
    @Test
    void createWallet() throws Exception {
        setUp();
        String uri = "/v1/wallet/create?uid=1";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(uri)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Wallet successfully created", content);
    }

    @Test
    void getWalletByWalletId() throws Exception {
        setUp();
        String uri = "/v1/wallet/get/wallet-id/1";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .get(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"walletId\":1,\"userId\":1,\"value\":0.0}", content);
    }

    @Test
    void getWalletByUserId() throws Exception {
        setUp();
        String uri = "/v1/wallet/get/user-id/1";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .get(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("{\"walletId\":1,\"userId\":1,\"value\":0.0}", content);
    }

    @Test
    void depositValueToWalletByWalletId() throws Exception {
        setUp();
        String uri = "/v1/wallet/deposit/wallet-id/1?value=22";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .put(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    @Test
    void depositValueToWalletByUserId() throws Exception {
        setUp();
        String uri = "/v1/wallet/deposit/user-id/1?value=22";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .put(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    @Test
    void debitedValueFromWalletByWalletId() throws Exception {
        setUp();
        String uri = "/v1/wallet/debited/wallet-id/1?debited-value=22";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .put(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    @Test
    void debitedValueFromWalletByUserId() throws Exception {
        setUp();
        String uri = "/v1/wallet/debited/user-id/1?debited-value=22";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .put(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    @Test
    void notFoundExceptionHandler() throws Exception {
        setUp();
        String uri = "/v1/wallet/get/wallet-id/2";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .get(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Wallet not found", content);
    }

    @Test
    void notEnoughValueHandler() throws Exception {
        setUp();
        String uri = "/v1/wallet/debited/user-id/1?debited-value=10";
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders
                                .put(uri)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("There are not enough funds on the wallet to complete the transaction", content);
    }


}