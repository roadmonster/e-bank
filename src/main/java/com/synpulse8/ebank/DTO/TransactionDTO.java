package com.synpulse8.ebank.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Serializer.CurrencySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO implements Cloneable{
    private UUID transaction_id;
    private Timestamp transaction_time;
    private Long userId;

    private Long account_id;
    @JsonSerialize(using = CurrencySerializer.class)
    private Currency currency;
    private BigDecimal amount;

    private String status;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
