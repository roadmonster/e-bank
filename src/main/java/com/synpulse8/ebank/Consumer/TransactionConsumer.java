package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.DepositWithdrawRequest;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.TransactionRepository;
import com.synpulse8.ebank.Services.Transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@AllArgsConstructor
public class TransactionConsumer {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    @KafkaListener(topics = "transaction", groupId = "transaction-group",
            containerFactory = "transactionKafkaListenerContainerFactory")
    public void handleTransactionEvent(DepositWithdrawRequest dto) {
        Transaction t = Transaction.builder()
                .transactionId(dto.getTransaction_id())
                .transactionTime(dto.getTransaction_time())
                .amount(dto.getAmount())
                .account(accountRepository.findById(dto.getAccount_id()).orElseThrow(
                        () -> new BankAccountNonExistException("no matching result in bank account database")
                ))
                .userid(dto.getUserId())
                .currency(dto.getCurrency())
                .build();

        transactionRepository.save(t);
        dto.setStatus("Posted");
        transactionService.updateTransactionStatus(dto.getTransaction_id(), dto);

    }
}
