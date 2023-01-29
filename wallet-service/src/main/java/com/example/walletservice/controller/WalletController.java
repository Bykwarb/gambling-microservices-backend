package com.example.walletservice.controller;

import com.example.walletservice.entity.Wallet;
import com.example.walletservice.exception.NotEnoughValueException;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.service.WalletService;
import com.example.walletservice.utils.WalletContextHolder;
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
    public ResponseEntity<Response> createWallet(@RequestParam("uid")Long userId){
        walletService.createWallet(userId);
        logger.debug("Create wallet with correlation-id: {}", WalletContextHolder.getContext().getCorrelationId());
        String message = "Wallet successfully created";
        return ResponseEntity.ok(new Response(message));
    }

    @GetMapping("/get/wallet-id/{walletId}")
    public ResponseEntity<Wallet> getWalletByWalletId(@PathVariable("walletId") Long walletId) throws WalletNotFoundException {
        logger.debug("Get wallet by wallet-id with correlation-id: {}", WalletContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(walletService.getWalletById(walletId));
    }

    @GetMapping("/get/user-id/{userId}")
    public ResponseEntity<Wallet> getWalletByUserId(@PathVariable("userId") Long userId) throws WalletNotFoundException {
        logger.debug("Get wallet by user-id with correlation-id: {}", WalletContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(walletService.getWalledByUserId(userId));
    }

    @PutMapping("/deposit/wallet-id/{walletId}")
    public ResponseEntity<WalletManipulationResponse> depositValueToWalletByWalletId(@PathVariable("walletId") Long walletId,
                                                                                     @RequestParam("value") Double value) throws WalletNotFoundException {
        Wallet wallet = walletService.depositToWalletByWalletId(walletId, value);
        logger.debug("Deposit to wallet by wallet-id with correlation-id: {}, and value: {}", WalletContextHolder.getContext().getCorrelationId(), value);
        String message = "Wallet successful replenished";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }

    @PutMapping("/deposit/user-id/{userId}")
    public ResponseEntity<WalletManipulationResponse> depositValueToWalletByUserId(@PathVariable("userId") Long userId,
                                                                                   @RequestParam("value") Double value) throws WalletNotFoundException {
        Wallet wallet = walletService.depositToWalletByUserId(userId, value);
        logger.debug("Deposit to wallet by user-id with correlation-id: {}, and value: {}", WalletContextHolder.getContext().getCorrelationId(), value);
        String message = "Wallet successful replenished";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }

    @PutMapping("/debited/wallet-id/{walletId}")
    public ResponseEntity<WalletManipulationResponse> debitedValueFromWalletByWalletId(@PathVariable("walletId") Long walletId,
                                                                                       @RequestParam("debited-value") Double value) throws WalletNotFoundException, NotEnoughValueException {
        Wallet wallet = walletService.payFromWalletByWalletId(walletId, value);
        logger.debug("Debited from wallet by wallet-id with correlation-id: {}, and value: {}", WalletContextHolder.getContext().getCorrelationId(), value);
        String message = "Value successful debited from wallet";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }

    @PutMapping("/debited/user-id/{userId}")
    public ResponseEntity<WalletManipulationResponse> debitedValueFromWalletByUserId(@PathVariable("userId") Long userId,
                                                                                       @RequestParam("debited-value") Double value) throws WalletNotFoundException, NotEnoughValueException {
        Wallet wallet = walletService.payFromWalletByUserId(userId, value);
        logger.debug("Debited from wallet by user-id with correlation-id: {}, and value: {}", WalletContextHolder.getContext().getCorrelationId(), value);
        String message = "Value successful debited from wallet";
        return ResponseEntity.ok(new WalletManipulationResponse(message, wallet));
    }


    @ExceptionHandler(value = {WalletNotFoundException.class})
    protected ResponseEntity<Response> notFoundExceptionHandler(){
        String bodyOfResponse = "Wallet not found";
        logger.debug("Wallet not found. Correlation-id: {}.", WalletContextHolder.getContext().getCorrelationId());
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
