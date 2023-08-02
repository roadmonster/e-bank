package com.synpulse8.ebank;

import com.synpulse8.ebank.Consumer.AccountConsumer;
import com.synpulse8.ebank.DTO.AccountCreation;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Exceptions.UserNotFoundException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Models.User;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.UserRepository;
import com.synpulse8.ebank.Services.Account.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountConsumerTest {

    @Autowired
    private AccountConsumer accountConsumer;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AccountService accountService;

    @Test
    void handleAccountCreatedEvent_ShouldProcessAccountCreationAndSaveToRepository() {
        // Arrange
        AccountCreation accountCreation = new AccountCreation();
        // Set the necessary properties for accountCreation
        accountCreation.setIban("IBAN123");
        accountCreation.setCurrency(Currency.USD);
        accountCreation.setUserId(1L);

        Account userAccount = new Account();
        // Set the necessary properties for userAccount
        userAccount.setIban("IBAN123");
        userAccount.setCurrency(Currency.USD);
        userAccount.setCredit_amt(BigDecimal.ONE);
        userAccount.setDebit_amt(BigDecimal.ZERO);
        // Assuming userRepository.findById returns the userAccount when called with 1L
        when(accountRepository.findById(1L)).thenReturn(Optional.of(userAccount));

        // Act
        accountConsumer.handleAccountCreatedEvent(accountCreation);

        // Assert
        // Verify that accountRepository.save was called with the correct Account object
        verify(accountRepository).save(Mockito.any(Account.class));

        // Verify that accountService.updateAccountCreationStatus was called with the correct AccountCreation object
        verify(accountService).updateAccountCreationStatus(accountCreation);
    }

    @Test
    void handleAccountCreatedEvent_WithNonExistingUser_ShouldThrowUserNotFoundException() {
        // Arrange
        AccountCreation accountCreation = new AccountCreation();
        // Set the necessary properties for accountCreation
        accountCreation.setIban("IBAN123");
        accountCreation.setCurrency(Currency.USD);
        accountCreation.setUserId(1L);

        // Assuming userRepository.findById returns an empty optional when called with 1L
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> accountConsumer.handleAccountCreatedEvent(accountCreation));
    }

    // Add more test cases for different scenarios as needed
}

