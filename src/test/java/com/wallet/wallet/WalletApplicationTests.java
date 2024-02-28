package com.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.wallet.dto.WalletDto;
import com.wallet.wallet.entity.WalletEntity;
import com.wallet.wallet.exceptions.InvalidRequestException;
import com.wallet.wallet.service.WalletService;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;

@WebMvcTest
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WalletService walletService;
    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void testUpdateWallet_Success() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletDto walletDto = new WalletDto();
        walletDto.setOperationType("DEPOSIT");
        walletDto.setAmount(BigDecimal.TEN);
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setWalletId(walletId);
        Mockito.when(walletService.getWalletById(walletId)).thenReturn(walletEntity);
        WalletEntity expectedWallet = new WalletEntity();
        expectedWallet.setWalletId(walletId);
        expectedWallet.setOperationType("DEPOSIT");
        expectedWallet.setAmount(walletDto.getAmount());
        Mockito.when(walletService.updateWallet(walletId, walletDto)).thenReturn(expectedWallet);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/wallet/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationType").value(walletDto.getOperationType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(walletDto.getAmount().doubleValue()));
    }

    @RepeatedTest(50)
    @Execution(ExecutionMode.CONCURRENT)
    void testUpdateWallet_f() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletDto walletDto = new WalletDto();
        walletDto.setOperationType("DEPOSIT");
        walletDto.setAmount(BigDecimal.TEN);
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setWalletId(walletId);
        Mockito.when(walletService.getWalletById(walletId)).thenReturn(walletEntity);
        WalletEntity expectedWallet = new WalletEntity();
        expectedWallet.setWalletId(walletId);
        expectedWallet.setOperationType("DEPOSIT");
        expectedWallet.setAmount(walletDto.getAmount());
        Mockito.when(walletService.updateWallet(walletId, walletDto)).thenReturn(expectedWallet);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/wallet/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationType").value(walletDto.getOperationType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(walletDto.getAmount().doubleValue()));
    }
    @Test
    void testCreateWallet_Success() throws Exception {
        WalletDto walletDto = new WalletDto();
        walletDto.setOperationType("DEPOSIT");
        walletDto.setAmount(BigDecimal.TEN);

        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setOperationType(walletDto.getOperationType());
        walletEntity.setAmount(walletDto.getAmount());

        Mockito.when(walletService.addWallet(walletDto)).thenReturn(walletEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationType").value(walletDto.getOperationType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(walletDto.getAmount().doubleValue()));
    }
    @Test
    void testCreateWallet_InvalidRequest() throws Exception {
        WalletDto walletDto = new WalletDto();
        walletDto.setOperationType("INVALID_OPERATION");
        walletDto.setAmount(BigDecimal.TEN);

        Mockito.when(walletService.addWallet(walletDto)).thenThrow(new InvalidRequestException("Invalid operationType"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}


/*
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.DataProvider;
public class WalletApplicationTests extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}*/

