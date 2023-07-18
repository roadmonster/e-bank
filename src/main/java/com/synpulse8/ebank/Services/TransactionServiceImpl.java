package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.BalanceUpdateDTO;
import com.synpulse8.ebank.DTO.TransactionDTO;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.TransactionRepository;
import com.synpulse8.ebank.Utilities.UUIDGenerator;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private Map<Long, Account> accountCache = new HashMap<>();
    private final KafkaTemplate<UUID, TransactionDTO> transactionKafkaTemplate;
    private final KafkaTemplate<UUID, BalanceUpdateDTO> accBalanceKafkaTemplate;
    private final ConcurrentMap<UUID, TransactionDTO> transactionData = new ConcurrentHashMap<>();

    @Override
    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findTransactionById(id);
    }

    @Override
    public List<Transaction> getAllTransactionByUser(Long userId) {
        return transactionRepository.findAllByUserid(userId);
    }

    @Override
    public List<Transaction> getTransactionByDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return transactionRepository.findTransactionByDate(date);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public void deposit(TransactionDTO transactionDto) {

        // Set initial status for the transaction
        transactionDto.setStatus("Pending");

        // Store the transaction details in the transactionData map
        transactionData.put(transactionDto.getTransaction_id(), transactionDto);

        // Publish the transaction event to a Kafka topic
        transactionKafkaTemplate.send("transaction", transactionDto.getTransaction_id(),transactionDto);
        BalanceUpdateDTO balanceUpdateDTO = BalanceUpdateDTO.builder()
                .account_id(transactionDto.getAccount_id())
                .amount(transactionDto.getAmount())
                .uuid(UUIDGenerator.generateUUID())
                .build();
        publishBalanceUpdate(balanceUpdateDTO);
    }

    @Override
    public void updateTransactionStatus(UUID uuid, TransactionDTO dto) {
        transactionData.put(uuid, dto);
    }

    @Override
    public void publishBalanceUpdate(BalanceUpdateDTO dto) {
        accBalanceKafkaTemplate.send("account_balance", dto);
    }

    public TransactionDTO getTransactionStatus(UUID transaction_id) {
        return this.transactionData.get(transaction_id);
    }


}
