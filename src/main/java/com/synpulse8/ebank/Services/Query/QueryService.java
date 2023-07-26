package com.synpulse8.ebank.Services.Query;

import com.synpulse8.ebank.DTO.QueryResponse;
import com.synpulse8.ebank.Models.Transaction;

import java.util.Date;
import java.util.List;

public interface QueryService {
    List<Transaction> queryTransactions(Date from, Date to, Long userId);
}
