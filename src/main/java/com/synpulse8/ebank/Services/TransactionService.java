package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.PersonalTransactionDTO;
import com.synpulse8.ebank.Models.Transaction;

import java.util.List;

public interface TransactionService {
//    boolean verifyTransaction(SendReceiveTransactionRequest request);
//    TransactionResponse createSendReceiveTransaction(SendReceiveTransactionRequest request);
//    Transaction createPersonalTransaction(PersonalTransactionRequest request);
    Transaction getTransactionById(Long id);
    List<Transaction> getTransactionByDate(int year, int month, int day);
    List<Transaction> getAllTransactions();
    void deposit(PersonalTransactionDTO transactionDto);
}
