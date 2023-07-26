package com.synpulse8.ebank.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.synpulse8.ebank.Enums.CountryCode;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Serializer.CountryCodeSerializer;
import com.synpulse8.ebank.Serializer.CurrencySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountCreation {
    @JsonSerialize(using = CurrencySerializer.class)
    private Currency currency;
    private Long userId;
    private String holderName;
    private String status;
    @JsonSerialize(using = CountryCodeSerializer.class)
    private CountryCode code;
    private String iban;
}
