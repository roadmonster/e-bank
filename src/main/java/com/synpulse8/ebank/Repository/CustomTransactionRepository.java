package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.Transaction;

import java.util.Date;
import java.util.List;

public interface CustomTransactionRepository {
    List<Transaction> findTransactionByDate(Date date);
}
