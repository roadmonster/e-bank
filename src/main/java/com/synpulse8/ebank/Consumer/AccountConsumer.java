package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.AccountCreation;
import com.synpulse8.ebank.Exceptions.UserNotFoundException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.UserRepository;
import com.synpulse8.ebank.Services.Account.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class AccountConsumer {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;

    @KafkaListener(topics = "account_creation", groupId = "account-group",
            containerFactory = "accountKafkaListenerContainerFactory")
    public void handleAccountCreatedEvent(AccountCreation accountCreation) {
        // Process the account created event
        Account acc = Account.builder()
                .iban(accountCreation.getIban())
                .currency(accountCreation.getCurrency())
                .credit_amt(BigDecimal.ONE)
                .debit_amt(BigDecimal.ZERO)
                .user(userRepository.findById(accountCreation.getUserId()).orElseThrow(
                        () -> new UserNotFoundException("Given userId not exist")
                ))
                .build();
        accountRepository.save(acc);
        accountCreation.setStatus("Processed");
        accountService.updateAccountCreationStatus(accountCreation);

    }
}
