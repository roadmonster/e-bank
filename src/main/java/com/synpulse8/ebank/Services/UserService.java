package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.UserUpdateRequest;
import com.synpulse8.ebank.Models.User;

public interface UserService {
    User getUser(Long id);
    User updateUser(UserUpdateRequest request);
}
