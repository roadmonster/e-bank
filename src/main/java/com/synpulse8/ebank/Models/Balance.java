package com.synpulse8.ebank.Models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Balance {
    private BigDecimal creditAmt;
    private BigDecimal debitAmt;
}
