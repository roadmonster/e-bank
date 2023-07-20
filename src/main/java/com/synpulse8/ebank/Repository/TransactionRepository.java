package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CustomTransactionRepository {
    List<Transaction> findByTransactionTimeBetween(Date d1, Date d2);
    List<Transaction> findAllByUserid(Long userId);
}
