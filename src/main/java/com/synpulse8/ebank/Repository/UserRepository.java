package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
