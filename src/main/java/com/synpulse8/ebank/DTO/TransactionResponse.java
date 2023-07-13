package com.synpulse8.ebank.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {
    private Long senderTransactionId;
    private Long receiverTransactionId;
}
