package com.wallet.wallet.service;

import com.wallet.wallet.dto.WalletDto;
import com.wallet.wallet.entity.WalletEntity;
import com.wallet.wallet.exceptions.InvalidRequestException;
import com.wallet.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public WalletEntity addWallet(WalletDto walletDto) {
        String operationType = walletDto.getOperationType();
        BigDecimal amount = walletDto.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("amount <= 0");
        } else if (!operationType.equals("DEPOSIT") && !operationType.equals("WITHDRAW")) {
            throw new IllegalArgumentException("Invalid operationType: " + operationType);
        } else {
            WalletEntity wallet = new WalletEntity();
            wallet.setOperationType(operationType);
            wallet.setAmount(walletDto.getAmount());
            walletRepository.save(wallet);
            return wallet;
        }
    }

    public WalletEntity updateWallet(UUID walletId, WalletDto walletDto) {
        WalletEntity wallet = getWalletById(walletId);
        wallet.setOperationType(walletDto.getOperationType());
        wallet.setAmount(walletDto.getAmount());
        walletRepository.save(wallet);
        return wallet;
    }


    public WalletEntity getWalletById(UUID walletId) {
        return walletRepository.findById(walletId).get();
    }
}
