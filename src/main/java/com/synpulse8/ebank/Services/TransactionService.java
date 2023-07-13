package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.PersonalTransactionRequest;
import com.synpulse8.ebank.DTO.SendReceiveTransactionRequest;
import com.synpulse8.ebank.DTO.TransactionResponse;
import com.synpulse8.ebank.Models.Transaction;

public interface TransactionService {
    boolean verifyTransaction(SendReceiveTransactionRequest request);
    TransactionResponse createSendReceiveTransaction(SendReceiveTransactionRequest request);
    Transaction createPersonalTransaction(PersonalTransactionRequest request);
    Transaction getTransactionById(Long id);
}
