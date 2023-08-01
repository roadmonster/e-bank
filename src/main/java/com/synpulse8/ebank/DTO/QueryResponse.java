package com.synpulse8.ebank.DTO;

import com.synpulse8.ebank.Models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResponse {
    private String requestId;
    private List<TransactionDto> transactions;
}
