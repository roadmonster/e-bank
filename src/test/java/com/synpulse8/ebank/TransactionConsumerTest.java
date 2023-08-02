package com.synpulse8.ebank;

import com.synpulse8.ebank.Consumer.TransactionConsumer;
import com.synpulse8.ebank.DTO.DepositWithdrawRequest;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.TransactionRepository;
import com.synpulse8.ebank.Services.Transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionConsumerTest {

    @Autowired
    private TransactionConsumer transactionConsumer;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    void handleTransactionEvent_ShouldProcessTransactionAndSaveToRepository() {
        // Arrange
        DepositWithdrawRequest dto = DepositWithdrawRequest.builder()
                .transaction_id(UUID.randomUUID().toString())
                .currency(Currency.USD)
                .amount(BigDecimal.valueOf(100))
                .userId(1L)
                .account_id(3L)
                .transaction_time(new Timestamp(System.currentTimeMillis()))
                .build();

        // Set up the behavior of accountRepository.findById
        Account account = new Account();
        // Set the necessary properties for account
        when(accountRepository.findById(dto.getAccount_id())).thenReturn(Optional.of(account));

        // Set up the behavior of transactionRepository.save
        Transaction savedTransaction = new Transaction();
        // Set the necessary properties for savedTransaction
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(savedTransaction);

        // Set up the behavior of transactionService.updateTransactionStatus
        doNothing().when(transactionService).updateTransactionStatus(Mockito.anyString(), Mockito.any(DepositWithdrawRequest.class));

        // Act
        transactionConsumer.handleTransactionEvent(dto);

        // Assert
        // Verify that transactionRepository.save was called with the correct Transaction object
        verify(transactionRepository).save(Mockito.any(Transaction.class));

        // Verify that transactionService.updateTransactionStatus was called with the correct arguments
        verify(transactionService).updateTransactionStatus(dto.getTransaction_id(), dto);
    }

    // Add more test cases for different scenarios as needed
}

