package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.BalanceUpdateDTO;
import com.synpulse8.ebank.DTO.TransactionDTO;
import com.synpulse8.ebank.Exceptions.BankTransactionNotFoundException;
import com.synpulse8.ebank.Models.Transaction;
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
    private final KafkaTemplate<UUID, TransactionDTO> transactionKafkaTemplate;
    private final KafkaTemplate<UUID, BalanceUpdateDTO> accBalanceKafkaTemplate;
    private final ConcurrentMap<UUID, TransactionDTO> transactionData = new ConcurrentHashMap<>();

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new BankTransactionNotFoundException("not matching recode for given transaction id")
        );
    }

    @Override
    public List<Transaction> getAllTransactionByUser(Long userId) {
        return transactionRepository.findAllByUserid(userId);
    }

    @Override
    public List<Transaction> getTransactionByDate(int year, int month, int day) {
        // create a locate date and convert to date object
        LocalDate localDate = LocalDate.of(year, month, day);

        // using the server's timezone could be a bit risky since the server
        // could be in different timezone, for the purpose of self contained app
        // we use server's timezone. In production will need front end to form this date
        // object and send it back.
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return transactionRepository.findByTransactionTimeBetween(date, date);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    /**
     * This deposit method work both way for deposit and withdraw, the client
     * will simply change the amount into negative.
     * It will update the data cache for the status for this transaction
     * then publish the message to transaction channel and the balance update channel
     */
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

    /**
     * The consumer shall invoke this method after execute the logic.
     * This method will update the local cache for the transaction status
     * @param uuid the uuid for this transaction
     * @param dto the data holding the status of the transaction from the consumer
     */
    @Override
    public void updateTransactionStatus(UUID uuid, TransactionDTO dto) {
        transactionData.put(uuid, dto);
    }

    /**
     * Publish the BalanceUpdate message into the channel
     * @param dto object holding the balance update details
     */
    @Override
    public void publishBalanceUpdate(BalanceUpdateDTO dto) {
        accBalanceKafkaTemplate.send("account_balance", dto);
    }


    public TransactionDTO getTransactionStatus(UUID transaction_id) {
        return this.transactionData.get(transaction_id);
    }

    @Override
    public List<Transaction> getTransactionBetween(Date fromDate, Date toDate) {
        return transactionRepository.findByTransactionTimeBetween(fromDate, toDate);
    }


}
