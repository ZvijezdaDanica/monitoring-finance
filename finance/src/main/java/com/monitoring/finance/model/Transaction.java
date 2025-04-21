package com.monitoring.finance.model;

import com.monitoring.finance.enums.Currencies;
import com.monitoring.finance.enums.TransactionTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    private TransactionTypes type;

    @NotNull(message = "Currency is required")
    @Enumerated(EnumType.STRING)
    private Currencies currency;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    private LocalDateTime date;

    @Positive(message = "Crypto price must be positive")
    private Double cryptoPrice;
}
