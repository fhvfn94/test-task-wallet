package com.wallet.wallet.service;

import com.wallet.wallet.dto.WalletDto;
import com.wallet.wallet.entity.WalletEntity;
import com.wallet.wallet.exceptions.InvalidRequestException;
import com.wallet.wallet.repository.WalletRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WalletService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    private void validateRequest(String operationType, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("amount <= 0");
        } else if (!operationType.equals("DEPOSIT") && !operationType.equals("WITHDRAW")) {
            throw new IllegalArgumentException("Invalid operationType: " + operationType);
        }
    }

    public WalletEntity updateWallet(UUID walletId, WalletDto walletDto) {
        if (walletId != null && walletRepository.existsById(walletId)) {
            CompletableFuture<WalletEntity> future = new CompletableFuture<>();
            executorService.execute(() -> {
                try {
                    String operationType = walletDto.getOperationType();
                    BigDecimal amount = walletDto.getAmount();
                    validateRequest(operationType, amount);
                    WalletEntity wallet = getWalletById(walletId);

                    if (operationType.equals("DEPOSIT")) {
                        wallet.setOperationType(operationType);
                        wallet.setAmount(wallet.getAmount().add(amount));
                    } else if (operationType.equals("WITHDRAW")) {
                        if (wallet.getAmount().compareTo(amount) >= 0) {
                            wallet.setOperationType(operationType);
                            wallet.setAmount(wallet.getAmount().subtract(amount));
                        } else {
                            throw new InvalidRequestException("amount <= 0");
                        }
                    }

                    // Сохраняем обновленный кошелек
                    WalletEntity updatedWallet = walletRepository.save(wallet);

                    // Завершаем CompletableFuture успешно
                    future.complete(updatedWallet);
                } catch (Exception e) {
                    // В случае ошибки завершаем CompletableFuture с исключением
                    future.completeExceptionally(e);
                }
            });

            try {
                // Блокируем основной поток, ожидая завершения CompletableFuture
                return future.get();
            } catch (Exception e) {
                // Обработка исключения
                throw new RuntimeException(e);
            }
        } else {
            // Кошелек не найден
            throw new RuntimeException(walletId + " not found");
        }
    }
    public WalletEntity addWallet(WalletDto walletDto) {
        String operationType = walletDto.getOperationType();
        BigDecimal amount = walletDto.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("amount <= 0");
        } else if (!operationType.equals("DEPOSIT")) {
            throw new IllegalArgumentException("Invalid operationType: " + operationType);
        } else {
            WalletEntity wallet = new WalletEntity();
            wallet.setOperationType(operationType);
            wallet.setAmount(walletDto.getAmount());
            walletRepository.save(wallet);
            return wallet;
        }
    }

    public WalletEntity getWalletById(UUID walletId) {
        return walletRepository.findById(walletId).get();
    }
}
