package com.example.gameservice.events;

import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class GameEvent extends ApplicationEvent {
    private Result result;
    private Session session;
    private String topic;
    public GameEvent(Result result, Session session, String topic) {
        super(result);
        this.result = result;
        this.session = session;
        this.topic = topic;
    }


}
