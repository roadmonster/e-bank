package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomTransactionRepositoryImpl implements CustomTransactionRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> findByTransactionTimeBetween(Date startDate, Date endDate) {

        Calendar startOfDay = Calendar.getInstance();
        startOfDay.setTime(startDate);
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);

        Calendar endOfDay = Calendar.getInstance();
        endOfDay.setTime(endDate);
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 999);

        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.transaction_time >= :startDate AND t.transaction_time <= :endDate", Transaction.class)
                .setParameter("startDate", startOfDay.getTime())
                .setParameter("endDate", endOfDay.getTime())
                .getResultList();
    }


}
