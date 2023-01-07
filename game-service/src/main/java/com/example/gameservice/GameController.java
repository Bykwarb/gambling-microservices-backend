package com.example.gameservice;

import com.example.gameservice.game.Result;
import com.example.gameservice.game.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/game/slot-machine")
public class GameController {

    @Autowired
    private GameService gameService;

    @CrossOrigin
    @GetMapping("/play")
    public ResponseEntity<Result> play(@RequestBody Session session){
        return ResponseEntity.ok(gameService.checkYourLuck(session));
    }

}
