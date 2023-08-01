package com.synpulse8.ebank.Services.Query;

import com.synpulse8.ebank.DTO.TransactionDto;
import java.util.Date;
import java.util.List;

public interface QueryService {
    List<TransactionDto> queryTransactions(Date from, Date to, Long userId);
}
