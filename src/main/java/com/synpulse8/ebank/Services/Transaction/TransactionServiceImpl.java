package com.synpulse8.ebank.Services.Transaction;

import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.DTO.DepositWithdrawRequest;
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
    private final KafkaTemplate<String, DepositWithdrawRequest> transactionKafkaTemplate;
    private final KafkaTemplate<String, BalanceUpdateRequest> accBalanceKafkaTemplate;
    private final ConcurrentMap<String, DepositWithdrawRequest> transactionData = new ConcurrentHashMap<>();
    /**
     * This deposit method work both way for deposit and withdraw, the client
     * will simply change the amount into negative.
     * It will update the data cache for the status for this transaction
     * then publish the message to transaction channel and the balance update channel
     */
    @Override
    public void deposit(DepositWithdrawRequest depositWithdrawRequest) {

        // Set initial status for the transaction
        depositWithdrawRequest.setStatus("Pending");

        // Store the transaction details in the transactionData map
        transactionData.put(depositWithdrawRequest.getTransaction_id(), depositWithdrawRequest);

        System.out.println("Debug content of request " + depositWithdrawRequest.toString());
        // Publish the transaction event to a Kafka topic
        transactionKafkaTemplate.send("transaction", depositWithdrawRequest.getTransaction_id(), depositWithdrawRequest);

        System.out.println("debug I have sent the transaction ");

        BalanceUpdateRequest balanceUpdateRequest = BalanceUpdateRequest.builder()
                .account_id(depositWithdrawRequest.getAccount_id())
                .amount(depositWithdrawRequest.getAmount())
                .uuid(UUIDGenerator.generateUUID())
                .build();
        publishBalanceUpdate(balanceUpdateRequest);
    }

    /**
     * The consumer shall invoke this method after execute the logic.
     * This method will update the local cache for the transaction status
     * @param uuid the uuid for this transaction
     * @param dto the data holding the status of the transaction from the consumer
     */
    @Override
    public void updateTransactionStatus(String uuid, DepositWithdrawRequest dto) {
        transactionData.put(uuid, dto);
    }

    /**
     * Publish the BalanceUpdate message into the channel
     * @param dto object holding the balance update details
     */
    @Override
    public void publishBalanceUpdate(BalanceUpdateRequest dto) {
        accBalanceKafkaTemplate.send("account_balance", dto);
    }

    public DepositWithdrawRequest getTransactionStatus(String transaction_id) {
        return this.transactionData.get(transaction_id);
    }
}
