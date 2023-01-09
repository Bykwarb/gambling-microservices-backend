package com.example.userservice.utils;

import org.springframework.util.Assert;

public class UserContextHolder {
    private static ThreadLocal<UserContext> walletContext = new ThreadLocal<>();
    public static final UserContext getContext(){
        UserContext context = walletContext.get();
        if (context == null){
            context = createEmptyContext();
            walletContext.set(context);
        }
        return walletContext.get();
    }
    public static final void setContext(UserContext context){
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        walletContext.set(context);
    }
    public static final UserContext createEmptyContext(){
        return new UserContext();
    }
}
