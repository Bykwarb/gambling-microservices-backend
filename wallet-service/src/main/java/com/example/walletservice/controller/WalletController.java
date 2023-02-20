package com.example.walletservice.controller;

import com.example.userservice.entities.WalletDto;
import com.example.userservice.utils.ClientContextHolder;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.service.WalletService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/wallet/")
public class WalletController {

    private Logger logger = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createWallet(@RequestBody WalletDto walletDto){
        walletService.createWallet(walletDto.getUserName());
        logger.debug("Create wallet to user {}, with correlation-id: {}", walletDto.getUserName(), ClientContextHolder.getContext().getCorrelationId());
        String message = "Wallet successfully created";
        return ResponseEntity.ok(new Response(message));
    }

    @GetMapping("/get/wallet-id/{walletId}")
    public ResponseEntity<Wallet> getWalletByWalletId(@PathVariable("walletId") Long walletId) throws WalletNotFoundException {
        logger.debug("Get wallet by wallet-id {}, with correlation-id: {}",  walletId, ClientContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(walletService.getWalletById(walletId));
    }

    @GetMapping("/get/user-name/{userName}")
    public ResponseEntity<Wallet> getWalletByUserName(@PathVariable("userName") String userName) throws WalletNotFoundException {
        logger.debug("Get wallet by user-name {}, with correlation-id: {}",  userName, ClientContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(walletService.getWalledByUserName(userName));
    }

    @PutMapping("/deposit/wallet-id/{walletId}")
    public ResponseEntity<WalletManipulationResponse> depositValueToWalletByWalletId(@PathVariable("walletId") Long walletId,
                                                                                     @RequestParam("value") Double value) throws WalletNotFoundException {
        Wallet wallet = walletService.depositToWalletByWalletId(walletId, value);
        logger.debug("Deposit to wallet by wallet-id with correlation-id: {}, and value: {}", ClientContextHolder.getContext().getCorrelationId(), value);
        String message = "Wallet successful replenished";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }

    @PutMapping("/deposit/user-name/{userName}")
    public ResponseEntity<WalletManipulationResponse> depositValueToWalletByUserId(@PathVariable("userName") String userName,
                                                                                   @RequestParam("value") Double value) throws WalletNotFoundException {
        Wallet wallet = walletService.depositToWalletByUserName(userName, value);
        logger.debug("Deposit to wallet by user-name {}, with correlation-id: {}, and value: {}", userName, ClientContextHolder.getContext().getCorrelationId(), value);
        String message = "Wallet successful replenished";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }

    @PutMapping("/debited/wallet-id/{walletId}")
    public ResponseEntity<WalletManipulationResponse> debitedValueFromWalletByWalletId(@PathVariable("walletId") Long walletId,
                                                                                       @RequestParam("debited-value") Double value) throws WalletNotFoundException, NotEnoughValueException {
        Wallet wallet = walletService.payFromWalletByWalletId(walletId, value);
        logger.debug("Debited from wallet by wallet-id {}, with correlation-id: {}, and value: {}",  walletId, ClientContextHolder.getContext().getCorrelationId(), value);
        String message = "Value successful debited from wallet";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }

    @PutMapping("/debited/user-name/{userName}")
    public ResponseEntity<WalletManipulationResponse> debitedValueFromWalletByUserId(@PathVariable("userName") String userName,
                                                                                       @RequestParam("debited-value") Double value) throws WalletNotFoundException, NotEnoughValueException {
        Wallet wallet = walletService.payFromWalletByUserName(userName, value);
        logger.debug("Debited from wallet by user-name {}, with correlation-id: {}, and value: {}",  userName, ClientContextHolder.getContext().getCorrelationId(), value);
        String message = "Value successful debited from wallet";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }


    @ExceptionHandler(value = {WalletNotFoundException.class})
    protected ResponseEntity<Response> notFoundExceptionHandler(){
        String bodyOfResponse = "Wallet not found";
        logger.debug("Wallet not found. Correlation-id: {}.",  ClientContextHolder.getContext().getCorrelationId());
        return new ResponseEntity<>(new Response(bodyOfResponse), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NotEnoughValueException.class})
    protected ResponseEntity<Response> notEnoughValueHandler(){
        String bodyOfResponse = "There are not enough funds on the wallet to complete the transaction";
        return new ResponseEntity<>(new Response(bodyOfResponse), HttpStatus.OK);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class WalletManipulationResponse{
        private String message;
        private Wallet wallet;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public class Response{
        private String message;
    }
}
