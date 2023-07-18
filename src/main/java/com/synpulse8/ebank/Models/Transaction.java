package com.synpulse8.ebank.Models;

import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Enums.MoneyType;
import com.synpulse8.ebank.Enums.TransactionType;
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
    private Timestamp transaction_time;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private Currency currency;
    private BigDecimal amount;
    private MoneyType money_type;
    private TransactionType transaction_type;
    private String sender_iban;
    private String receiver_iban;

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

