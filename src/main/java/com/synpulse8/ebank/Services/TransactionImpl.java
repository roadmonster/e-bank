package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.PersonalTransactionRequest;
import com.synpulse8.ebank.DTO.SendReceiveTransactionRequest;
import com.synpulse8.ebank.DTO.TransactionResponse;
import com.synpulse8.ebank.Enums.TransactionType;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Exceptions.BankTransactionNotFoundException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Models.Balance;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private Map<Long, Balance> accountBalanceCache = new HashMap<>();

    @Override
    public boolean verifyTransaction(SendReceiveTransactionRequest request) {
        return false;
    }

    @Override
    public TransactionResponse createSendReceiveTransaction(SendReceiveTransactionRequest request) {
        Account senderAcc = accountRepository.findById(request.getSenderId()).orElseThrow(
                () -> new BankAccountNonExistException("Account id " +  request.getSenderId() + " not found"));

        Transaction payTransaction = Transaction.builder()
                .amount(request.getAmount().negate())
                .currency(request.getCurrency())
                .transaction_type(request.getTransactionType())
                .money_type(request.getMoneyType())
                .transaction_time(request.getTimestamp())
                .sender_id(request.getSenderId())
                .receiver_id(request.getReceiverId())
                .account(senderAcc)
                .build();

        Transaction sendRecord = transactionRepository.save(payTransaction);

        Account receiverAcc = accountRepository.findById(request.getReceiverId()).orElseThrow(
                () -> new BankAccountNonExistException("Account id " +  request.getReceiverId() + " not found"));

        Transaction receiveTransaction = Transaction.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .transaction_type(request.getTransactionType())
                .money_type(request.getMoneyType())
                .transaction_time(request.getTimestamp())
                .sender_id(request.getSenderId())
                .receiver_id(request.getReceiverId())
                .account(receiverAcc)
                .build();

        Transaction receiveRecord = transactionRepository.save(receiveTransaction);

        return TransactionResponse.builder()
                .senderTransactionId(sendRecord.getId())
                .receiverTransactionId(receiveRecord.getId())
                .build();
    }

    @Override
    public Transaction createPersonalTransaction(PersonalTransactionRequest request) {
        Account acc = accountRepository.findById(request.getAccountId()).orElseThrow(
                () -> new BankAccountNonExistException("Account id not found"));
        TransactionType type = request.getTransactionType();
        BigDecimal amount = request.getAmount();
        if (type == TransactionType.Withdraw) {
            amount = amount.negate();
        }

        Transaction t = Transaction.builder()
                .amount(amount)
                .currency(request.getCurrency())
                .transaction_type(type)
                .transaction_time(request.getTimestamp())
                .account(acc)
                .build();

        return transactionRepository.save(t);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new BankTransactionNotFoundException("Give transaction id not exisit")
        );
    }


}
