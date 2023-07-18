package com.synpulse8.ebank.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Serializer.CurrencySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendReceiveTransactionDto {
    private Long transaction_id;
    private Long from_account;
    private Long to_account;
    @JsonSerialize(using = CurrencySerializer.class)
    private Currency currency;
    private BigDecimal amount;
    private Timestamp transaction_time;

}
