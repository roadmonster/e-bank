package com.synpulse8.ebank.DTO;

import com.synpulse8.ebank.Enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String transactionId;
    private Timestamp transactionTime;
    private BigDecimal amount;
    private Currency currency;
    private Long userid;
    private Long accountId;
}
