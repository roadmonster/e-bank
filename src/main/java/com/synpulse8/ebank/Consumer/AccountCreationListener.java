package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.AccountCreationDTO;
import com.synpulse8.ebank.Exceptions.UserNotFoundException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class AccountCreationListener {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = "account_creation", groupId = "account-group",
            containerFactory = "accountKafkaListenerContainerFactory")
    public void handleAccountCreatedEvent(AccountCreationDTO accountCreationDTO) {

        System.out.println("Here is my iban " + accountCreationDTO.getIban());

        // Process the account created event
        Account acc = Account.builder()
                .iban(accountCreationDTO.getIban())
                .currency(accountCreationDTO.getCurrency())
                .credit_amt(BigDecimal.ONE)
                .debit_amt(BigDecimal.ZERO)
                .user(userRepository.findById(accountCreationDTO.getUserId()).orElseThrow(
                        () -> new UserNotFoundException("Given userId not exist")
                ))
                .build();
        accountRepository.save(acc);
        accountCreationDTO.setStatus("Processed");

    }
}
