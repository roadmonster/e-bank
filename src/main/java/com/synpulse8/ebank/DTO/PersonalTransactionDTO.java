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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalTransactionDTO implements Cloneable{
    private Long transaction_id;
    private String iban;
    @JsonSerialize(using = CurrencySerializer.class)
    private Currency currency;
    private BigDecimal amount;
    private Timestamp transaction_time;
    private String status;

    @Override
    public Object clone() throws CloneNotSupportedException {
        PersonalTransactionDTO clonedObject = (PersonalTransactionDTO) super.clone();
        return clonedObject;
    }
}
