package com.synpulse8.ebank.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Enums.MoneyDirection;
import com.synpulse8.ebank.Serializer.CurrencySerializer;
import com.synpulse8.ebank.Serializer.MoneyDirectionSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendReceiveRequest {
    @JsonSerialize(using = MoneyDirectionSerializer.class)
    private MoneyDirection direction;
    private Long from_account;
    private Long to_account;
    @JsonSerialize(using = CurrencySerializer.class)
    private Currency currency;
    private BigDecimal amount;
    private Long sender_id;
    private Long receiver_id;
    private Timestamp transaction_time;

}
