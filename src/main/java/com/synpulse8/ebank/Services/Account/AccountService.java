package com.synpulse8.ebank.Services.Account;

import com.synpulse8.ebank.DTO.AccountCreation;
import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.Models.Account;

import java.util.List;

public interface AccountService {
    Account getAccountById(Long id);
    List<Account> getAccountForUser(Long userId);
    void accountCreation(AccountCreation request);
    AccountCreation getCreationStatus(String iban);
    Account getAccountByIban(String iban);
    void updateAccountCreationStatus(AccountCreation accountCreation);
    void updateBalance(BalanceUpdateRequest balanceUpdateRequest);
}
