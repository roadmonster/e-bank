package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CustomTransactionRepository {
    List<Transaction> findTransactionByDate(Date date);
    Transaction findTransactionById(UUID id);
    List<Transaction> findAllByUserid(Long userId);
}
