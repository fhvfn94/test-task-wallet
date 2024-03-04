package com.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.wallet.dto.WalletDto;
import com.wallet.wallet.entity.WalletEntity;
import com.wallet.wallet.repository.WalletRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WalletRepository walletRepository;

    @Test
    void testUpdateWallets() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    UUID walletId = UUID.fromString("f162e670-02f7-46b5-bf90-fa19d9c79bb4");
                    WalletDto walletDto = new WalletDto("DEPOSIT", BigDecimal.TEN);
                    mockMvc.perform(put("/api/v1/wallet/{walletId}", walletId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(walletDto)))
                            .andExpect(status().isOk());
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        thread.start();
    }

    @Test
    void testUpdateWallets_f() throws Exception {
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                try {
                    UUID walletId = UUID.fromString("f162e670-02f7-46b5-bf90-fa19d9c79bb4");
                    WalletDto walletDto = new WalletDto("DEPOSIT", BigDecimal.TEN);
                    mockMvc.perform(put("/api/v1/wallet/{walletId}", walletId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(walletDto)))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    throw new RuntimeException();
                } finally {
                    latch.countDown();
                }
            });
            thread.start();
        }
        latch.await();
        UUID walletId = UUID.fromString("f162e670-02f7-46b5-bf90-fa19d9c79bb4");
        WalletEntity wallet = walletRepository.findById(walletId).get();
        assertEquals(BigDecimal.valueOf(111), wallet.getAmount());

    }
}

