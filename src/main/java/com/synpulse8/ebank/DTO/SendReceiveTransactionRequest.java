package com.synpulse8.ebank.DTO;

import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Enums.MoneyType;
import com.synpulse8.ebank.Enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendReceiveTransactionRequest {
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
    private MoneyType moneyType;
    private TransactionType transactionType;
    private Timestamp transactionTime;
    private Currency currency;
}
