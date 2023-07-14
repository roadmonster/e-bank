package com.synpulse8.ebank.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private String username;
    private String pwd;
    private String email;
    private Long id;
}
