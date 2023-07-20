package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.BalanceUpdateDTO;
import com.synpulse8.ebank.DTO.TransactionDTO;
import com.synpulse8.ebank.Models.Transaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
//    boolean verifyTransaction(SendReceiveTransactionRequest request);
    Transaction getTransactionById(Long uuid);
    List<Transaction> getTransactionByDate(int year, int month, int day);
    List<Transaction> getTransactionBetween(Date fromDate, Date toDate);
    List<Transaction> getAllTransactionByUser(Long userId);
    List<Transaction> getAllTransactions();
    void deposit(TransactionDTO transactionDto);
    void updateTransactionStatus(UUID uuid, TransactionDTO dto);
    void publishBalanceUpdate(BalanceUpdateDTO dto);
    TransactionDTO getTransactionStatus(UUID transaction_id);

}
