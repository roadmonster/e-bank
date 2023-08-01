package com.synpulse8.ebank.Services.Transaction;

import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.DTO.DepositWithdrawRequest;
import com.synpulse8.ebank.Models.Transaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
//    boolean verifyTransaction(SendReceiveTransactionRequest request);
//    Transaction getTransactionById(Long uuid);
//    List<Transaction> getTransactionByDate(int year, int month, int day);
//    List<Transaction> getTransactionBetween(Date fromDate, Date toDate);
//    List<Transaction> getAllTransactionByUser(Long userId);
//    List<Transaction> getAllTransactions();
    void deposit(DepositWithdrawRequest depositWithdrawRequest);
    void updateTransactionStatus(UUID uuid, DepositWithdrawRequest dto);
    void publishBalanceUpdate(BalanceUpdateRequest dto);
    DepositWithdrawRequest getTransactionStatus(UUID transaction_id);

}
