package com.example.gameservice.events;

import com.example.gameservice.game.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SendGameToGameHistoryEvent extends ApplicationEvent {
    private Result result;
    public SendGameToGameHistoryEvent(Result result) {
        super(result);
        this.result = result;
    }


}
