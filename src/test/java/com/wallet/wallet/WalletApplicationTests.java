package com.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.wallet.controller.WalletController;
import com.wallet.wallet.dto.WalletDto;
import com.wallet.wallet.entity.WalletEntity;
import com.wallet.wallet.exceptions.InvalidRequestException;
import com.wallet.wallet.repository.WalletRepository;
import com.wallet.wallet.service.WalletService;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @RepeatedTest(50)
    @Execution(ExecutionMode.CONCURRENT)
    void testUpdateWallet_f() throws Exception {
        UUID walletId = UUID.fromString("f162e670-02f7-46b5-bf90-fa19d9c79bb4");
        WalletDto walletDto = new WalletDto("DEPOSIT", BigDecimal.TEN);

        mockMvc.perform(put("/api/v1/wallet/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(status().isOk());
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

