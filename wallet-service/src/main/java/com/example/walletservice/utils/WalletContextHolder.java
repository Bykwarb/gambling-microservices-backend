package com.example.walletservice.utils;

import org.springframework.util.Assert;

public class WalletContextHolder {
    private static ThreadLocal<WalletContext> walletContext = new ThreadLocal<>();
    public static final WalletContext getContext(){
        WalletContext context = walletContext.get();
        if (context == null){
            context = createEmptyContext();
            walletContext.set(context);
        }
        return walletContext.get();
    }
    public static final void setContext(WalletContext context){
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        walletContext.set(context);
    }
    public static final WalletContext createEmptyContext(){
        return new WalletContext();
    }
}
