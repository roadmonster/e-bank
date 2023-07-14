package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.UserUpdateRequest;
import com.synpulse8.ebank.Exceptions.UserNotFoundException;
import com.synpulse8.ebank.Models.User;
import com.synpulse8.ebank.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Not matching user with given user id")
        );
    }

    @Override
    public User updateUser(UserUpdateRequest request) {
        Long userId = request.getId();
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setName(request.getUsername());
                    existingUser.setEmail(request.getEmail());
                    existingUser.setPwd(passwordEncoder.encode(request.getPwd()));
                    return userRepository.save(existingUser);
                })
                .orElseThrow(()->new UserNotFoundException("No matching user with given userId in our data"));
    }
}
