package com.synpulse8.ebank.Services.Account;

import com.synpulse8.ebank.DTO.AccountCreation;
import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, AccountCreation> accountKafkaTemplate;
    private final ConcurrentMap<String, AccountCreation> accountData = new ConcurrentHashMap<>();

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new BankAccountNonExistException("Account not found"));
    }

    @Override
    public List<Account> getAccountForUser(Long userId) {
        return accountRepository.findAllAccountsByUserId(userId);
    }

    @Override
    public void accountCreation(AccountCreation accountCreation) {
        // Set initial status for the transaction
        accountCreation.setStatus("Processing");

        // Store the transaction details in the transactionData map
        accountData.put(accountCreation.getIban(), accountCreation);

        // Publish the transaction event to a Kafka topic
        accountKafkaTemplate.send("account_creation", accountCreation.getIban(), accountCreation);
    }

    @Override
    public AccountCreation getCreationStatus(String iban) {
        return this.accountData.get(iban);
    }

    @Override
    public Account getAccountByIban(String iban) {
        return accountRepository.findAccountByIban(iban).orElseThrow(
                () -> new BankAccountNonExistException(iban + " not existing")
        );
    }

    @Override
    public void updateAccountCreationStatus(AccountCreation accountCreation) {
        accountData.put(accountCreation.getIban(), accountCreation);
    }

    @Override
    public void updateBalance(BalanceUpdateRequest dto) {
        Account acc = accountRepository.findById(dto.getAccount_id())
                .orElseThrow(() -> new BankAccountNonExistException("Account non existent"));
        BigDecimal newBalance = acc.getDebit_amt().add(dto.getAmount());
        acc.setDebit_amt(newBalance);
        accountRepository.save(acc);
    }
}
