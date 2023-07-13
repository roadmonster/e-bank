package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.AccountRequest;
import com.synpulse8.ebank.Models.Account;

public interface AccountService {
    Account createAccount(AccountRequest request);
    Account getAccountById(Long id);
}
