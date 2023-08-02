package com.synpulse8.ebank;

import com.synpulse8.ebank.DTO.AccountCreation;
import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Services.Account.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private KafkaTemplate<String, AccountCreation> accountKafkaTemplate;

    @Spy
    private final ConcurrentMap<String, AccountCreation> accountData = new ConcurrentHashMap<>();

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountCreation mockAccountCreation;

    @BeforeEach
    void setUp() {
        mockAccountCreation = createMockAccountCreation("iban123");
    }

    private AccountCreation createMockAccountCreation(String iban) {
        AccountCreation mockAccountCreation = new AccountCreation();
        mockAccountCreation.setIban(iban);
        // Set other properties as needed for the test
        return mockAccountCreation;
    }

    @Test
    void testGetAccountById_ExistingAccount() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        Account result = accountService.getAccountById(accountId);

        // Assert
        assertEquals(mockAccount, result);
    }

    @Test
    void testGetAccountById_NonExistingAccount() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BankAccountNonExistException.class, () -> accountService.getAccountById(accountId));
    }

    @Test
    void testGetAccountForUser() {
        // Arrange
        Long userId = 1L;
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account());
        when(accountRepository.findAllAccountsByUserId(userId)).thenReturn(mockAccounts);

        // Act
        List<Account> result = accountService.getAccountForUser(userId);

        // Assert
        assertEquals(mockAccounts, result);
    }

    @Test
    void testAccountCreation() {
        // Arrange
        String iban = "iban123";
        when(accountKafkaTemplate.send(any(), any(), any())).thenReturn(null);

        // Act
        accountService.accountCreation(mockAccountCreation);

        // Assert
        assertEquals("Processing", mockAccountCreation.getStatus());
        assertTrue(accountData.containsKey(iban));
        verify(accountKafkaTemplate).send("account_creation", iban, mockAccountCreation);
    }

    @Test
    void testGetCreationStatus() {
        // Arrange
        String iban = "iban123";
        accountData.put(iban, mockAccountCreation);

        // Act
        AccountCreation result = accountService.getCreationStatus(iban);

        // Assert
        assertEquals(mockAccountCreation, result);
    }

    @Test
    void testGetAccountByIban_ExistingAccount() {
        // Arrange
        String iban = "iban123";
        Account mockAccount = new Account();
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(mockAccount));

        // Act
        Account result = accountService.getAccountByIban(iban);

        // Assert
        assertEquals(mockAccount, result);
    }

    @Test
    void testGetAccountByIban_NonExistingAccount() {
        // Arrange
        String iban = "iban123";
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BankAccountNonExistException.class, () -> accountService.getAccountByIban(iban));
    }

    @Test
    void testUpdateAccountCreationStatus() {
        // Arrange
        String iban = "iban123";

        // Act
        accountService.updateAccountCreationStatus(mockAccountCreation);

        // Assert
        assertTrue(accountData.containsKey(iban));
        assertEquals(mockAccountCreation, accountData.get(iban));
    }

    @Test
    void testUpdateBalance_ExistingAccount() {
        // Arrange
        Long accountId = 1L;
        BigDecimal initialBalance = new BigDecimal("1000.00");
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setDebit_amt(initialBalance);

        BalanceUpdateRequest dto = new BalanceUpdateRequest();
        dto.setAccount_id(accountId);
        dto.setAmount(new BigDecimal("500.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        accountService.updateBalance(dto);

        // Assert
        BigDecimal expectedBalance = initialBalance.add(dto.getAmount());
        assertEquals(expectedBalance, mockAccount.getDebit_amt());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void testUpdateBalance_NonExistingAccount() {
        // Arrange
        Long accountId = 1L;
        BalanceUpdateRequest dto = new BalanceUpdateRequest();
        dto.setAccount_id(accountId);
        dto.setAmount(new BigDecimal("500.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BankAccountNonExistException.class, () -> accountService.updateBalance(dto));
    }
}

