package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.TransactionDTO;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.TransactionRepository;
import com.synpulse8.ebank.Services.TransactionService;
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
    public void handleTransactionEvent(TransactionDTO dto) {
        Transaction t = Transaction.builder()
                .id(dto.getTransaction_id())
                .transaction_time(dto.getTransaction_time())
                .amount(dto.getAmount())
                .account(accountRepository.findAccountByIban(dto.getIban()).orElseThrow(
                        () -> new BankAccountNonExistException("no matching result in bank account database")
                ))
                .userid(dto.getUserId())
                .currency(dto.getCurrency())
                .build();

        System.out.println("before " + t.toString());
        transactionRepository.save(t);
        System.out.println("after");
        dto.setStatus("Posted");
        transactionService.updateTransactionStatus(dto.getTransaction_id(), dto);

    }
}
