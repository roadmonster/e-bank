package com.synpulse8.ebank.Controllers;

import com.synpulse8.ebank.DTO.SignupRequest;
import com.synpulse8.ebank.DTO.UserUpdateRequest;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Models.User;
import com.synpulse8.ebank.Services.Account.AccountService;
import com.synpulse8.ebank.Services.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @PutMapping
    public User updateUser(@RequestBody UserUpdateRequest request) {
        return userService.updateUser(request);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public User createUser(@RequestBody SignupRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{userId}/accounts")
    public List<Account> getAllAccountsForUser(@PathVariable Long userId){
        return accountService.getAccountForUser(userId);
    }





}
