package com.synpulse8.ebank.DTO;

import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalTransactionRequest {
    private TransactionType transactionType;
    private Timestamp timestamp;
    private BigDecimal amount;
    private Currency currency;
    private Long accountId;
}
