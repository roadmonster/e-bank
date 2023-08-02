package com.synpulse8.ebank.Services.Transaction;

import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.DTO.DepositWithdrawRequest;

public interface TransactionService {
    void deposit(DepositWithdrawRequest depositWithdrawRequest);
    void updateTransactionStatus(String uuid, DepositWithdrawRequest dto);
    void publishBalanceUpdate(BalanceUpdateRequest dto);
    DepositWithdrawRequest getTransactionStatus(String transaction_id);

}
