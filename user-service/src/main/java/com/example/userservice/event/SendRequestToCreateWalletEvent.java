package com.example.userservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SendRequestToCreateWalletEvent extends ApplicationEvent {
    private String username;

    public SendRequestToCreateWalletEvent(String username) {
        super(username);
        this.username = username;
    }
}
