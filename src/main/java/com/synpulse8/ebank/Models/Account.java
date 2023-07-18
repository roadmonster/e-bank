package com.synpulse8.ebank.Models;

import com.synpulse8.ebank.Enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Currency currency;
    private BigDecimal credit_amt;
    private BigDecimal debit_amt;
    private String iban;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}


