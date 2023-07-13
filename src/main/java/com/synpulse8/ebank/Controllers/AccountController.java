package com.synpulse8.ebank.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebank.DTO.AccountRequest;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountRequest accountRequest) {
        Account acc = accountService.createAccount(accountRequest);
        try {
            return new ResponseEntity<>(getAccountJson(acc), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<String> getAccountById(@PathVariable Long accountId) {
        try {
            return new ResponseEntity<>(getAccountJson(accountService.getAccountById(accountId)), HttpStatus.OK);
        } catch (BankAccountNonExistException e) {
            return new ResponseEntity<>("Bank account not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getAccountJson(Account acc) throws JsonProcessingException {
        String accountJson = objectMapper.writeValueAsString(acc);
        return accountJson;
    }

}
