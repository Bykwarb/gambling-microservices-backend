package com.example.gameservice;

import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import com.example.gameservice.game.SlotMachine;
import com.example.gameservice.utils.Response;
import com.example.userservice.utils.ClientContextHolder;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class GameService {
    @Autowired
    private SlotMachine slotMachine;
    @Value("${wallet.uri}")
    private String walletUri;
    private RestTemplate restTemplate;
    private Result result;
    private Logger logger = LoggerFactory.getLogger(GameService.class);

    public Result checkYourLuck(Session session){
        Response response = debitedFromWallet(session);
        if (response.getMessage().equals("There are not enough funds on the wallet to complete the transaction")){
            return notEnoughMoney();
        }
        if (session.getLines() > 5 || session.getLines() < 1){
            return incorrectLines();
        }
        result = slotMachine.play(session);
        if (result.getStatus() == Result.Status.Win){
            Response response1 = depositToWallet(session, result);
            logger.debug(response1.getMessage());
        }
        return result;
    }
    private Result notEnoughMoney(){
        result = new Result();
        result.setStatus(Result.Status.NotEnoughMoney);
        logger.debug("Not enough money. Correlation-id: {}.", ClientContextHolder.getContext().getCorrelationId());
        return result;
    }
    private Result incorrectLines(){
        result = new Result();
        result.setStatus(Result.Status.IncorrectLinesNumber);
        logger.debug("Incorrect lines number. Correlation-id: {}.", ClientContextHolder.getContext().getCorrelationId());
        return result;
    }
    private Result serviceUnavailable(){
        result = new Result();
        result.setStatus(Result.Status.ServiceUnavailable);
        logger.debug("Wallet service unavailable. Correlation-id: {}.", ClientContextHolder.getContext().getCorrelationId());
        return result;
    }

    private Response depositToWallet(Session session, Result result){
        restTemplate = new RestTemplate();
        String url = walletUri + "/deposit/user-name/" + session.getBetterUserName() + "?value=" + result.getResult();
        return getResponse(url);
    }

    private Response debitedFromWallet(Session session){
        restTemplate = new RestTemplate();
        String url = walletUri + "/debited/user-name/" + session.getBetterUserName() + "?debited-value=" + session.getBetValue();
        return getResponse(url);
    }

    @Nullable
    private Response getResponse(String url) {
        ResponseEntity<Response> walletResponse;
        try {
            walletResponse = restTemplate.exchange(url, HttpMethod.PUT, getEntity(), Response.class);
        }catch (HttpServerErrorException ex){
            if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                return null;
            } else {
                throw ex;
            }
        }
        return walletResponse.getBody();
    }
    private HttpEntity<String> getEntity(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", ClientContextHolder.getContext().getAuthToken());
        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        return entity;
    }


}
