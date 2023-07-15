package com.synpulse8.ebank.DTO;

import com.synpulse8.ebank.Enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private Currency currency;
    private Long userId;
}
