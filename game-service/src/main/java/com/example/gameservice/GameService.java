package com.example.gameservice;

import com.example.gameservice.events.GameEvent;
import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import com.example.gameservice.game.SlotMachine;
import com.example.gameservice.utils.Response;
import com.example.userservice.utils.ClientContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GameService {
    @Autowired
    private final SlotMachine slotMachine;
    @Value("${wallet.uri}")
    private String walletUri;
    private RestTemplate restTemplate;
    private Result result;
    @Autowired
    private final ApplicationEventPublisher publisher;

    public GameService(SlotMachine slotMachine, ApplicationEventPublisher publisher) {
        this.slotMachine = slotMachine;
        this.publisher = publisher;
    }


    public Result checkYourLuck(Session session){
        Response response = debitedFromWallet(session);
        if (response == null){
            return handleError(Result.Status.ServiceUnavailable);
        }
        if (response.getMessage().equals("There are not enough funds on the wallet to complete the transaction")){
            return handleError(Result.Status.NotEnoughMoney);
        }
        if (session.getLines() > 5 || session.getLines() < 1){
            return handleError(Result.Status.IncorrectLinesNumber);
        }
        result = slotMachine.play(session);
        if (result.getStatus() == Result.Status.Win){
            depositToWallet(session, result);
        }
        return result;
    }
    private Result handleError(Result.Status status){
        result = new Result();
        result.setStatus(status);
        log.debug("{}. Correlation-id: {}.", status, ClientContextHolder.getContext().getCorrelationId());
        return result;
    }

    private void depositToWallet(Session session, Result result){
        publisher.publishEvent(new GameEvent(result, session,"wallet-message"));
    }

    private Response debitedFromWallet(Session session){
        restTemplate = new RestTemplate();
        String url = walletUri + "/debited/user-name/" + session.getBetterUserName() + "?debited-value=" + session.getBetValue();
        return getResponse(url);
    }

    private Response getResponse(String url) {
        HttpEntity<String> entity = getEntity();
        try {
            ResponseEntity<Response> walletResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, Response.class);
            return walletResponse.getBody();
        } catch (HttpServerErrorException ex) {
            log.error("Failed to call wallet service: {}", ex.getMessage());
            return null;
        }
    }
    private HttpEntity<String> getEntity(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", ClientContextHolder.getContext().getAuthToken());
        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        return entity;
    }


}
