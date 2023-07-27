package com.synpulse8.ebank.Services.User;

import com.synpulse8.ebank.DTO.RegisterRequest;
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
        System.out.println("request username :" + request.getName());
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setUsername(request.getName());
                    existingUser.setEmail(request.getEmail());
                    existingUser.setPwd(passwordEncoder.encode(request.getPwd()));
                    return userRepository.save(existingUser);
                })
                .orElseThrow(()->new UserNotFoundException("No matching user with given userId in our data"));
    }

    @Override
    public User createUser(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .pwd(passwordEncoder.encode(request.getPwd()))
                .email(request.getEmail())
                .build();
        return userRepository.save(user);
    }
}
