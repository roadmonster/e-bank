package com.synpulse8.ebank.Controllers;

import com.synpulse8.ebank.DTO.UserUpdateRequest;
import com.synpulse8.ebank.Models.User;
import com.synpulse8.ebank.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public User updateUser(@RequestBody UserUpdateRequest request) {
        return userService.updateUser(request);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }


}
