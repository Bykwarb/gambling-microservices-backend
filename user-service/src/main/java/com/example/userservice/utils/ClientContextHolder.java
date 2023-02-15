package com.example.userservice.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ClientContextHolder {
    private static ThreadLocal<ClientContext> clientContext = new ThreadLocal<>();
    public static final ClientContext getContext(){
        ClientContext context = clientContext.get();
        if (context == null){
            context = createEmptyContext();
            clientContext.set(context);
        }
        return clientContext.get();
    }
    public static final void setContext(ClientContext context){
        Assert.notNull(context, "Only non-null ClientContext instances are permitted");
        clientContext.set(context);
    }
    public static final ClientContext createEmptyContext(){
        return new ClientContext();
    }
}
