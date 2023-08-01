package com.synpulse8.ebank.Models;

import com.synpulse8.ebank.Enums.Currency;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private Timestamp transactionTime;
    private Long userid;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private Currency currency;
    private BigDecimal amount;
}

