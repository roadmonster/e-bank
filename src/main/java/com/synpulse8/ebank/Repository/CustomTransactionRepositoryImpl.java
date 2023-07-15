package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

public class CustomTransactionRepositoryImpl implements CustomTransactionRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> findTransactionByDate(Date date) {
        return null;
    }
}
