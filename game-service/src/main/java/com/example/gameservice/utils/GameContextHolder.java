package com.example.gameservice.utils;

import org.springframework.util.Assert;

public class GameContextHolder {
    private static ThreadLocal<GameContext> walletContext = new ThreadLocal<>();
    public static final GameContext getContext(){
        GameContext context = walletContext.get();
        if (context == null){
            context = createEmptyContext();
            walletContext.set(context);
        }
        return walletContext.get();
    }
    public static final void setContext(GameContext context){
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        walletContext.set(context);
    }
    public static final GameContext createEmptyContext(){
        return new GameContext();
    }
}
