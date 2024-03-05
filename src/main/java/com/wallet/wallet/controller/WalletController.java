package com.wallet.wallet.controller;

import com.wallet.wallet.dto.WalletDto;
import com.wallet.wallet.entity.WalletEntity;
import com.wallet.wallet.exceptions.InvalidRequestException;
import com.wallet.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "api/v1")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(path = "wallet")
    public ResponseEntity<WalletEntity> createWallet(@RequestBody WalletDto walletDto) {
        try {
            return new ResponseEntity<>(walletService.addWallet(walletDto), HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(path = "wallet/{walletId}")
    public WalletEntity updateWallet(@PathVariable UUID walletId, @RequestBody WalletDto walletDto) {
        return walletService.updateWallet(walletId, walletDto);
    }

    @GetMapping(path = "wallets/{walletId}")
    public WalletEntity getWalletById(@PathVariable UUID walletId) {
        return walletService.getWalletById(walletId);
    }
}
