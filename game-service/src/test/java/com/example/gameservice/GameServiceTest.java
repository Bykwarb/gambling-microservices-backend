package com.example.gameservice;

import com.example.gameservice.events.GameEvent;
import com.example.gameservice.game.SlotMachine;
import com.example.gameservice.game.entity.Result;
import com.example.gameservice.game.entity.Session;
import com.example.gameservice.game.utils.Lines;
import com.example.gameservice.game.utils.Symbols;
import com.example.gameservice.utils.Response;
import com.example.userservice.UserService;
import com.example.userservice.utils.ClientContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private SlotMachine slotMachine;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private Response response;

    @Mock
    private GameEvent gameEvent;

    @Captor
    private ArgumentCaptor<GameEvent> gameEventCaptor;

    @InjectMocks
    private GameService gameService;

    private Session session;

    Symbols[][] grid = {
            { Symbols.CHERRY, Symbols.SEVEN, Symbols.LEMON },
            { Symbols.CHERRY, Symbols.CHERRY, Symbols.CHERRY },
            { Symbols.SEVEN, Symbols.BELLS, Symbols.BAR}
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new Session();
        session.setBetterUserName("player1");
        session.setBetValue(5);
        session.setLines(1);
    }

    @Test
    void checkYourLuck_ServiceUnavailable() {
        when(restTemplate.exchange(any(), any(), any(), (Class<Object>) any())).thenThrow(HttpServerErrorException.class);
        Result result = gameService.checkYourLuck(session);
        assertEquals(Result.Status.ServiceUnavailable, result.getStatus());
    }

    @Test
    void checkYourLuck_Successful() {
        when(restTemplate.exchange(any(),  any(), any(), (Class<Object>) any())).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        when(response.getMessage()).thenReturn("Ok");
        Result mockResult = new Result();
        mockResult.setStatus(Result.Status.Win);
        Map<Lines, Symbols> winLines = new HashMap<>();
        winLines.put(Lines.Second, Symbols.CHERRY);
        mockResult.setSymbols(grid);
        mockResult.setWinLines(winLines);
        when(slotMachine.play(any())).thenReturn(mockResult);
        doNothing().when(publisher).publishEvent(any());
        Result result = gameService.checkYourLuck(session);
        assertEquals(Result.Status.Win, result.getStatus());
        assertNotNull(result.getWinLines());
        assertNotNull(result.getWinLines().get(Lines.Second));
        verify(publisher).publishEvent(gameEventCaptor.capture());
        assertEquals(gameEvent, gameEventCaptor.getValue());
    }
}
