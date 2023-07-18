package com.synpulse8.ebank.Models;

import com.synpulse8.ebank.Enums.Currency;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Transaction")
public class Transaction {
    @Id
    private UUID id;
    private Timestamp transaction_time;
    private Long userid;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private Currency currency;
    private BigDecimal amount;
//    private TransactionType transaction_type;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Transient
    private Date transactionDate;

    public Date getTransactionDate() {
        return transaction_time != null ? new Date(transaction_time.getTime()) : null;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}

