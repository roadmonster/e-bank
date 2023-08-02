package com.synpulse8.ebank;

import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.DTO.DepositWithdrawRequest;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Repository.TransactionRepository;
import com.synpulse8.ebank.Services.Transaction.TransactionServiceImpl;
import org.apache.kafka.common.utils.SystemTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private KafkaTemplate<String, DepositWithdrawRequest> transactionKafkaTemplate;

    @Mock
    private KafkaTemplate<String, BalanceUpdateRequest> accBalanceKafkaTemplate;

    @Spy
    private ConcurrentMap<String, DepositWithdrawRequest> transactionData = new ConcurrentHashMap<>();

    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionKafkaTemplate,accBalanceKafkaTemplate);
    }

    @Test
    void deposit_ShouldPublishTransactionEventAndBalanceUpdate() {
        // Arrange
        String transactionId = UUID.randomUUID().toString();
        DepositWithdrawRequest depositWithdrawRequest = DepositWithdrawRequest.builder()
                .account_id(3L)
                .userId(1L)
                .amount(BigDecimal.valueOf(100))
                .currency(Currency.USD)
                .transaction_id(transactionId)
                .transaction_time(new Timestamp(SystemTime.SYSTEM.milliseconds()))
                .build();

        when(transactionKafkaTemplate.send(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(DepositWithdrawRequest.class))
        ).thenReturn(null);

        when(accBalanceKafkaTemplate.send(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(BalanceUpdateRequest.class))
        ).thenReturn(null);

        // Act
        transactionService.deposit(depositWithdrawRequest);

        // Assert
        verify(transactionKafkaTemplate).send("transaction", depositWithdrawRequest.getTransaction_id(), depositWithdrawRequest);
        System.out.println("Actual interactions with accBalanceKafkaTemplate:");
        verify(accBalanceKafkaTemplate, Mockito.atLeastOnce()).send(Mockito.any(), Mockito.any());

    }
}


