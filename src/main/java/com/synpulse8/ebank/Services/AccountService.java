package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.AccountRequest;
import com.synpulse8.ebank.Models.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountRequest request);
    Account getAccountById(Long id);
    List<Account> getAccountForUser(Long userId);
}
