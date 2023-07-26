package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class CustomTransactionRepositoryImpl implements CustomTransactionRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> findByTransactionTimeBetween(Date startDate, Date endDate) {

        Timestamp startTimestamp = getTimestampWithStartTime(startDate);
        Timestamp endTimestamp = getTimestampWithEndTime(endDate);

        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.transaction_time >= :startDate AND t.transaction_time <= :endDate", Transaction.class)
                .setParameter("startDate", startTimestamp)
                .setParameter("endDate", endTimestamp)
                .getResultList();
    }

    @Override
    public List<Transaction> findTransactionByUserIdDuringDate(Long userId, Date startDate, Date endDate) {
        Timestamp startTimestamp = getTimestampWithStartTime(startDate);
        Timestamp endTimestamp = getTimestampWithEndTime(endDate);

        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.transaction_time BETWEEN :startDate AND :endDate", Transaction.class)
                .setParameter("startDate", startTimestamp)
                .setParameter("endDate", endTimestamp)
                .getResultList();
    }

    // Helper method to set the time part of the Timestamp to the beginning of the day (00:00:00)
    private Timestamp getTimestampWithStartTime(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        return Timestamp.valueOf(startOfDay);
    }

    // Helper method to set the time part of the Timestamp to the end of the day (23:59:59.999999999)
    private Timestamp getTimestampWithEndTime(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        return Timestamp.valueOf(endOfDay);
    }


}
