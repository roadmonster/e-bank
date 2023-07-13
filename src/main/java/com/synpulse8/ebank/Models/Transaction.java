package com.synpulse8.ebank.Models;

import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Enums.MoneyType;
import com.synpulse8.ebank.Enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private Timestamp transaction_time;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private Currency currency;
    private BigDecimal amount;
    private MoneyType money_type;
    private TransactionType transaction_type;
    private Long sender_id;
    private Long receiver_id;
}

