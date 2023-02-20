package com.example.userservice;

import com.example.userservice.event.SendRequestToCreateWalletEvent;
import com.example.userservice.utils.ClientContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserServiceApplication.class)
public class WalletEventTest {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Test
    public void test(){
        ClientContextHolder.getContext().setAuthToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCeWt3YXJiIiwicm9sZSI6IlVTRVIiLCJrZXkiOiIkMmEkMTAkZUFVdEFhcjB0cTNOcDMxVk90RDJBLjlNc0hMZGlJYWF4aVp1MW9qWUVzTzhMYWxBaFBPTHEiLCJpYXQiOjE2NzY1NzMwMTEsImV4cCI6MTY3NjY1OTQxMX0.DD1Dgb48lQK2bQBIoQ1q2N5V2q5l-uu_0UsHQfNP6is");
        publisher.publishEvent(new SendRequestToCreateWalletEvent("test"));
    }
}
