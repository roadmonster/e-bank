package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.AccountCreationDTO;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Exceptions.UserNotFoundException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Models.User;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.UserRepository;
import com.synpulse8.ebank.Utilities.IBANGenerator;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, AccountCreationDTO> kafkaTemplate;
    private final ConcurrentMap<String, AccountCreationDTO> accountData = new ConcurrentHashMap<>();

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
    public void accountCreation(AccountCreationDTO accountCreationDTO) {

        // Set initial status for the transaction
        accountCreationDTO.setStatus("Processing");


        // Store the transaction details in the transactionData map
        accountData.put(accountCreationDTO.getIban(), accountCreationDTO);

        // Publish the transaction event to a Kafka topic
        kafkaTemplate.send("account_creation", accountCreationDTO.getIban(),accountCreationDTO);

        System.out.println("I am here in the acc serv");
    }

    @Override
    public AccountCreationDTO getCreationStatus(String iban) {
        return this.accountData.get(iban);
    }
}
