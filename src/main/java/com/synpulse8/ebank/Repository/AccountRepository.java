package com.synpulse8.ebank.Repository;

import com.synpulse8.ebank.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllAccountsByUserId(Long userId);
}
